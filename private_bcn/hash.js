const { rejects } = require('assert');

const fs = require('fs');
const crypto = require('crypto');
const { resolve } = require('dns');
const { error } = require('console');

// Path to image folder
const imageFolderPath = 'D:\\GITHUB\\Specialized-Project\\model\\yolov5\\capture';

// Hash calculator function
const calculateImageHash = (imagePath) => {
    return new Promise((resolve, rejects) => {
        const hash = crypto.createHash('sha256');
        const stream = fs.createReadStream(imagePath);

        stream.on('data', (data) => {
            hash.update(data);
        });

        stream.on('end', () => {
            const imageHash = hash.digest('hex');
            resolve(imageHash);
        });

        stream.on('erorr', (error) => {
            rejects(error);
        });
    });
};

// Get final image & hash calculate
const getLastImageHash = async () => {
    try{
        const imageFiles = fs.readdirSync(imageFolderPath);

        if(imageFiles.length > 0) {
            const lastImageFile = imageFiles[imageFiles.length - 1];
            const lastImagePath = `${imageFolderPath}/${lastImageFile}`
            const imageHash = await calculateImageHash(lastImagePath);

            console.log(`Last Image: ${lastImageFile}, Hash: ${imageHash}`);
        } else {
            console.log('No images found in the folder');
        }
    } catch (error) {
        console.error('Error', error);
    }
};

getLastImageHash();