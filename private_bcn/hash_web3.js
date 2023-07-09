// Import thư viện web3.js và khởi tạo đối tượng web3
const Web3 = require('web3');
const web3 = new Web3('http://127.0.0.1:7545'); // Thay đổi URL của mạng blockchain của bạn nếu cần thiết

// Địa chỉ của contract đã triển khai
const contractAddress = '0xce473f276a57bF21b463b8e612d1E7E2dDdfBE1B'; // Thay đổi địa chỉ của contract đã triển khai của bạn

const fs = require('fs');

const contractData = fs.readFileSync('build/contracts/HashStorage.json', 'utf8');
const parsedContractData = JSON.parse(contractData);

// ABI của contract
const contractAbi = parsedContractData.abi;

// Khởi tạo đối tượng contract từ ABI và địa chỉ
const contract = new web3.eth.Contract(contractAbi, contractAddress);

// Phương thức thêm hash vào contract
const addHash = async (hash) => {
    const accounts = await web3.eth.getAccounts();
    const result = await contract.methods.addHash(hash).send({ from: accounts[0], gas: 200000 });
    console.log('Transaction hash:', result.transactionHash);
  };
  
  // Phương thức lấy thông tin hash từ contract
  const getHash = async (id) => {
    const hashInfo = await contract.methods.getHash(id).call();
    console.log('Hash:', hashInfo[0]);
    console.log('Timestamp:', new Date(parseInt(hashInfo[1]) * 1000));
  };
  
  // Phương thức lấy tất cả hash từ contract
  const getAllHashes = async () => {
    const result = await contract.methods.getAllHashes().call();
    const hashes = result.map(hashInfo => ({
      hash: hashInfo[0],
      timestamp: new Date(parseInt(hashInfo[1]) * 1000)
    }));
    console.log('All Hashes:', hashes);
  };
  
  // Phương thức xóa hash từ contract
  const deleteHash = async (id) => {
    const accounts = await web3.eth.getAccounts();
    const result = await contract.methods.deleteHash(id).send({ from: accounts[0] });
    console.log('Transaction hash:', result.transactionHash);
  };
  
  // Gọi các phương thức
  // addHash('abc123');
  // getHash(1);
  // getAllHashes();
  // deleteHash(1);
  
  // Xử lý tham số dòng lệnh
  const handleCommandLineArguments = () => {
    const args = process.argv.slice(2); // Lấy danh sách tham số dòng lệnh
  
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
  
  // Gọi hàm xử lý tham số dòng lệnh
  handleCommandLineArguments();