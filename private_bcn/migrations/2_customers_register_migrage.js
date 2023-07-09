// Help Truffle find `TruffleTutorial.sol` in the `/contracts` directory
const CustomersRegistry = artifacts.require("CustomersRegistry");

module.exports = function(deployer) {
  // Command Truffle to deploy the Smart Contract
  deployer.deploy(CustomersRegistry);
};