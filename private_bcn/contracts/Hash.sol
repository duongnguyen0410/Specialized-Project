//SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;
pragma experimental ABIEncoderV2;

contract HashStorage {
    struct HashInfo {
        string hash;
        uint256 timestamp;
    }

    mapping(uint256 => HashInfo) private hashes;
    uint256 private hashCount;

    event HashAdded(uint256 indexed id, string hash, uint256 timestamp);
    event HashDeleted(uint256 indexed id);

    function addHash(string memory hash) public {
        hashCount++;
        hashes[hashCount] = HashInfo(hash, block.timestamp);
        emit HashAdded(hashCount, hash, block.timestamp);
    }

    function getHash(uint256 id) public view returns (string memory, uint256) {
        require(id <= hashCount, "Invalid hash ID");
        HashInfo memory hashInfo = hashes[id];
        return (hashInfo.hash, hashInfo.timestamp);
    }

    function getAllHashes() public view returns (HashInfo[] memory) {
        HashInfo[] memory allHashes = new HashInfo[](hashCount);
        for (uint256 i = 1; i <= hashCount; i++) {
            allHashes[i - 1] = hashes[i];
        }
        return allHashes;
    }

    function deleteHash(uint256 id) public {
        require(id <= hashCount, "Invalid hash ID");
        delete hashes[id];
        emit HashDeleted(id);
    }
}