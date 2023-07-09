// Import thư viện web3.js và khởi tạo đối tượng web3
const Web3 = require('web3');
const web3 = new Web3('http://127.0.0.1:7545'); // Thay đổi URL của mạng blockchain của bạn nếu cần thiết

// Địa chỉ của contract đã triển khai
const contractAddress = '0x94F46451658CcD060aF26aE38E8149C19517bD8f'; // Thay đổi địa chỉ của contract đã triển khai của bạn

const fs = require('fs');

const contractData = fs.readFileSync('build/contracts/CustomerRegistry.json', 'utf8');
const parsedContractData = JSON.parse(contractData);

// ABI của contract
const contractAbi = parsedContractData.abi;

// Khởi tạo đối tượng contract từ ABI và địa chỉ
const contract = new web3.eth.Contract(contractAbi, contractAddress);

// Phương thức thêm thông tin khách hàng
const addCustomer = async (name, age, gender) => {
  const accounts = await web3.eth.getAccounts();
  const result = await contract.methods.addCustomer(name, age, gender).send({ from: accounts[0], gas: 200000 });
  console.log('Transaction hash:', result.transactionHash);

  const customerId = result.events.CustomerAdded.returnValues.customerId;
  console.log('Customer ID:', customerId);

  // Kiểm tra trạng thái giao dịch
  const receipt = await web3.eth.getTransactionReceipt(result.transactionHash);
  console.log('Transaction status:', receipt.status);
};

// Phương thức sửa thông tin khách hàng
const updateCustomer = async (customerId, name, age, gender) => {
  const accounts = await web3.eth.getAccounts();
  const result = await contract.methods.updateCustomer(customerId, name, age, gender).send({ from: accounts[0] });
  console.log('Transaction hash:', result.transactionHash);
};

// Phương thức xóa thông tin khách hàng
const deleteCustomer = async (customerId) => {
  const accounts = await web3.eth.getAccounts();
  const result = await contract.methods.deleteCustomer(customerId).send({ from: accounts[0] });
  console.log('Transaction hash:', result.transactionHash);

  console.log('Customer deleted:', customerId);
};

// Phương thức lấy thông tin khách hàng
const getCustomer = async (customerId) => {
  const customer = await contract.methods.getCustomer(customerId).call();
  console.log('Customer:', customer);
};

// Phương thức lấy tất cả thông tin khách hàng
const getAllCustomers = async () => {
  const result = await contract.methods.getAllCustomers().call();

  const customerIds = result[0];
  const customers = result[1];

  for (let i = 0; i < customerIds.length; i++) {
    console.log('Customer:', `[ id: ${customerIds[i]}, name: '${customers[i].name}', age: '${customers[i].age}', gender: '${customers[i].gender}' ]`);
  }
};

// Gọi các phương thức
// addCustomer('James', 10, 'Male');
// updateCustomer(0, 'Jane Smith', 35, 'Female');
// deleteCustomer(1);
// getCustomer(2);
// getAllCustomers();\

// Xử lý tham số dòng lệnh
const handleCommandLineArguments = () => {
  const args = process.argv.slice(2); // Lấy danh sách tham số dòng lệnh

  if (args.length > 0) {
    const command = args[0];
    const params = args.slice(1);

    switch (command) {
      case 'add':
        addCustomer(...params);
        break;
      case 'update':
        updateCustomer(...params);
        break;
      case 'delete':
        deleteCustomer(...params);
        break;
      case 'get':
        getCustomer(...params);
        break;
      case 'getAll':
        getAllCustomers();
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