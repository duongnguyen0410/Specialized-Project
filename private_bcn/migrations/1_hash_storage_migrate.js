// Help Truffle find `TruffleTutorial.sol` in the `/contracts` directory
const HashStorage = artifacts.require("HashStorage");

module.exports = function(deployer) {
  // Command Truffle to deploy the Smart Contract
  deployer.deploy(HashStorage);
};