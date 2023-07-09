//SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;
pragma experimental ABIEncoderV2;

contract CustomerRegistry {
    struct Customer {
        string name;
        uint age;
        string gender;
    }

    mapping(uint256 => Customer) private customers;
    uint256 private nextCustomerId;

    event CustomerAdded(uint256 customerId);
    event CustomerUpdated(uint256 customerId);
    event CustomerDeleted(uint256 customerId);

    function addCustomer(string memory _name, uint _age, string memory _gender) public returns (uint256) {
        Customer storage customer = customers[nextCustomerId];
        customer.name = _name;
        customer.age = _age;
        customer.gender = _gender;

        emit CustomerAdded(nextCustomerId);
        nextCustomerId++;

        return nextCustomerId - 1;
    }

    function updateCustomer(uint256 _customerId, string memory _name, uint _age, string memory _gender) public {
        require(_customerId < nextCustomerId, "Invalid customer ID");

        Customer storage customer = customers[_customerId];
        customer.name = _name;
        customer.age = _age;
        customer.gender = _gender;

        emit CustomerUpdated(_customerId);
    }

   function deleteCustomer(uint256 _customerId) public {
        require(_customerId < nextCustomerId, "Invalid customer ID");

        // Di chuyển khách hàng cuối cùng trong danh sách vào vị trí cần xóa
        customers[_customerId] = customers[nextCustomerId - 1];
        delete customers[nextCustomerId - 1];

        nextCustomerId--;

        emit CustomerDeleted(_customerId);
    }

    function getCustomer(uint256 _customerId) public view returns (string memory, uint, string memory) {
        require(_customerId < nextCustomerId, "Invalid customer ID");

        Customer storage customer = customers[_customerId];
        return (customer.name, customer.age, customer.gender);
    }

    function getAllCustomers() public view returns (uint256[] memory, Customer[] memory) {
        uint256[] memory customerIds = new uint256[](nextCustomerId);
        Customer[] memory allCustomers = new Customer[](nextCustomerId);

        for (uint256 i = 0; i < nextCustomerId; i++) {
            customerIds[i] = i;
            allCustomers[i] = customers[i];
        }

        return (customerIds, allCustomers);
    }
}