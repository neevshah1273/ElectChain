# importing libraries

import datetime
import hashlib
import json
from flask import Flask, jsonify

# creating blockchain

class Blockchain:

    def __init__(self): # initializing blockchain

        self.chain = []
        self.createBlock(proof = 1, previousHash = '0') # creating the genesis block

    def createBlock(self, proof, previousHash): # create a block in the blockchain and define its contents

        block = {
            'index': len(self.chain) + 1,
            'timestamp': str(datetime.datetime.now()),
            'proof_of_work': proof,
            'previous_hash': previousHash
        }
        self.chain.append(block) # adding the block in the blockchain
        return block # returning the block so that we could see it in the Postman
    
    def getPreviousBlock(self):

        return self.chain[-1]

    def proofOfWork(self, previousProof): # defining proof of work

        newProof = 1
        checkProof = False
        while checkProof is False:
            work = newProof**2 - previousProof**2 # non-symmetric math puzzle (or work) to be solved by miner
            workHash = hashlib.sha256(str(work).encode()).hexdigest() # hash digest of the work
            if workHash[:5] == '00000': # check if there are 5 leading 0s for proof of work
                checkProof = True
            else:
                newProof += 1
        return newProof

    def hash(self, block): # function to calculate hash digest of the block for integrity
        
        encodedBlock = json.dumps(block, sort_keys = True).encode() # converting block into format expected by hashlib.sha256 function
        blockHash = hashlib.sha256(encodedBlock).hexdigest() # calculating the hash digest of the block
        return blockHash

    def chainValidity(self, chain): # to check whether chain is valid

        previousBlock = chain[0]
        currentBlockIndex = 1
        while currentBlockIndex < len(chain):
            currentBlock = chain[currentBlockIndex]
             # checking previous_hash field in current block
            if currentBlock['previous_hash'] != self.hash(previousBlock):
                return False
            # checking proof of work
            previousProof = previousBlock['proof_of_work']
            currentProof = currentBlock['proof_of_work']
            work = currentProof**2 - previousProof**2
            workHash = hashlib.sha256(str(work).encode()).hexdigest()
            if workHash[:5] != '00000':
                return False
            previousBlock = currentBlock
            currentBlockIndex += 1
        return True # if chain is valid