const Web3 = require('web3');
const web3 = new Web3('http://127.0.0.1:7545'); 

const contractAddress = '0x9bbe041f71A1755672b37dc5Ec944F2D391BAA86'; 

const fs = require('fs');

const contractData = fs.readFileSync('build/contracts/HashStorage.json', 'utf8');
const parsedContractData = JSON.parse(contractData);

const contractAbi = parsedContractData.abi;

const contract = new web3.eth.Contract(contractAbi, contractAddress);

const addHash = async (hash) => {
    const accounts = await web3.eth.getAccounts();
    const result = await contract.methods.addHash(hash).send({ from: accounts[0], gas: 200000 });
    console.log('Transaction hash:', result.transactionHash);
  };
  
  const getHash = async (id) => {
    const hashInfo = await contract.methods.getHash(id).call();
    console.log('Hash:', hashInfo[0]);
    console.log('Timestamp:', new Date(parseInt(hashInfo[1]) * 1000));
  };
  
  const getAllHashes = async () => {
    const result = await contract.methods.getAllHashes().call();
    const hashes = result.map(hashInfo => ({
      hash: hashInfo[0],
      timestamp: new Date(parseInt(hashInfo[1]) * 1000)
    }));
    console.log('All Hashes:', hashes);
  };
  
  const deleteHash = async (id) => {
    const accounts = await web3.eth.getAccounts();
    const result = await contract.methods.deleteHash(id).send({ from: accounts[0] });
    console.log('Transaction hash:', result.transactionHash);
  };
  
  const handleCommandLineArguments = () => {
    const args = process.argv.slice(2);
  
    if (args.length > 0) {
      const command = args[0];
      const params = args.slice(1);
  
      switch (command) {
        case 'add':
          addHash(...params);
          break;
        case 'get':
          getHash(...params);
          break;
        case 'getAll':
          getAllHashes();
          break;
        case 'delete':
          deleteHash(...params);
          break;
        default:
          console.log('Invalid command');
      }
    } else {
      console.log('No command specified');
    }
  };
  
  handleCommandLineArguments();