/*
 * This class generates 16 roundkey for encryptin and decryption
*/
package desimplementation;


public class KeyGenerator {

    private int[] leftKey = new int[28];
    private int[] rightKey = new int[28];
    private int[] key64 = new int[64];
    private int[] key56 = new int[56];
    private int[][] allRoundKey = new int[16][48];

    public KeyGenerator(String keyWord) {
        key64 = getEncrpytedKeyword(keyWord);
        key56 = getPermutedBy_PC1(key64);
        doKeySegementation(key56);

        for (int round = 1; round <= 16; round++) {
            allRoundKey[round - 1] = getRoundKey(round);
        }
    }
    
    public int[] getRoundKeyForEncryption(int roundNumber) {
        return allRoundKey[roundNumber - 1];
    }

    public int[] getRoundKeyForDecryption(int roundNumber) {
        return allRoundKey[16 - roundNumber];
    }

    public int[] getRoundKey(int roundNumber) {
        int[] roundKey = new int[48];
        doLeftShift(roundNumber);
        roundKey = getPermutedBy_PC2(combineLeftRight());

        return roundKey;
    }

    //this methdo converts string key to binary block
    public int[] getEncrpytedKeyword(String keyWord) {
        byte[][] block = new byte[8][8];
        int[] encryptKey = new int[64];

        for (int i = 0; i < 8 && i < keyWord.length(); i++) {
            block[i] = getBinaryBits(keyWord.charAt(i));
        }

        int index = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                encryptKey[index] = (int) block[i][j];
                index++;
            }
        }
        return encryptKey;
    }

    
    //this method converts a chatacter to 8 byte array
    public byte[] getBinaryBits(int ch) {
        byte[] bin = new byte[8];
        for (int i = 0; i < 8; i++) {
            bin[7 - i] = (byte) ((ch >> i) & 1);
        }
        return bin;
    }

    
    
    //this method divide 56 bit key to two 28 bit left and right key
    public void doKeySegementation(int[] key56) {
        int index = 0;
        for (int i = 0; i < 28; i++) {
            leftKey[i] = key56[i];
        }

        for (int i = 28; i < 56; i++) {
            rightKey[index] = key56[i];
            index++;
        }
    }

    
    //this method do all necessary left shift
    public void doLeftShift(int round) {
        int leftShiftNumber = AllData.numOfLeftRotation[(round - 1)];
        if (leftShiftNumber == 1) {
            doOneLeftShitf(leftKey, rightKey);

        } else {
            doOneLeftShitf(leftKey, rightKey);
            doOneLeftShitf(leftKey, rightKey);
        }
    }

    
    //this method performs one left shift operation
    public void doOneLeftShitf(int[] side1, int[] side2) {

        int temp = side1[0];
        for (int i = 1; i < side1.length; i++) {
            side1[i - 1] = side1[i];

        }
        side1[side1.length - 1] = temp;
        temp = side2[0];
        for (int i = 1; i < side2.length; i++) {
            side2[i - 1] = side2[i];
        }
        side2[side2.length - 1] = temp;
    }

    
    
    
    //combine 56 bits key
    public int[] combineLeftRight() {

        int[] key56 = new int[56];
        int index = 28;

        for (int i = 0; i < 28; i++) {
            key56[i] = leftKey[i];
        }

        for (int i = 0; i < 28; i++) {
            key56[index] = rightKey[i];
            index++;
        }

        return key56;
    }

    
    
    //return 56 bits key
    public int[] getPermutedBy_PC1(int[] key_in) {
        int[] store_num = AllData.getPermutedChoice1Table();
        int[] key_out = new int[56];
        int temp = 0;
        int i = 0;
        int loop = 0;
        int check = 0;

        while (check != 56) {
            temp = store_num[i];
            if (temp == loop) {
                key_out[check] = key_in[loop - 1];
                loop = 0;
                check++;
                i++;
            }
            loop++;
        }
        return key_out;
    }

    
    
    //return 48 bits key
    public int[] getPermutedBy_PC2(int[] key_in) {
        int[] store_num = AllData.getPermutedChoice2Table();
        int[] key_out = new int[48];
        int temp = 0;
        int i = 0;
        int loop = 0;
        int check = 0;

        while (check != 48) {
            temp = store_num[i];
            if (temp == loop) {
                key_out[check] = key_in[loop - 1];
                loop = 0;
                check++;
                i++;
            }
            loop++;
        }
        return key_out;
    }

}
