package com.mysirui.vehicle.util;

/**
 * OneWay 单向加密
 */
public class Ht2crypt_btk {
    
    /****************************************************************************
    * Table which contains the EXOR value of a 4 bit input.                     *
    ****************************************************************************/
    private static byte[] exor_table = {0, 1, 1, 0, 1, 0, 0, 1,
                      	               	1, 0, 0, 1, 0, 1, 1, 0};
    
    /****************************************************************************
    * Non-linear functions F0, F1 and F2.                                       *
    *                                                                           *
    * The logic "one" entries of F0 and F1 have multiple bits set which         *
    * makes it unnecessary to shift the values when combining the results       *
    * to the input vector for F2.                                               *
    * (Only bit masking necessary, see function_bit(). )                        *
    ****************************************************************************/
    private static final int A = 1+16;
    private static final int B = 2+4+8;
    private static byte[] F0_table = {A, 0, 0, A, A, A, A, 0, 0, 0, A, A, 0, A, 0, 0};
    private static byte[] F1_table = {B, 0, 0, 0, B, B, B, 0, 0, B, B, 0, 0, B, B, 0};
    private static byte[] F2_table = {1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0,
        							  1, 1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 1, 1, 1, 0};
    
    /****************************************************************************
    * Main parameters for HITAG2 cryptographic algorithm.                       *
    ****************************************************************************/
    private static byte[] ident = {0, 0, 0, 0};      //Transponder identifier 
    private static final byte[] Base_Password = {0x55, 0x66};
    private static final byte[] Immo_BasePassword = {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};	//Immo ƒ£ Ωœ¬µƒª˘’æ√‹¬Î
    private static final byte[] Trans_Password = {0x44, 0x44};
    private static byte[] t = new byte[2];  /* The 48 bit ...     */
    private static byte[] s = new byte[4];  /* ... shift register */
    
    private static byte GETBIT(byte a, int b) {
    	return (byte) ((((a) & (1<<(b) ))!=0)?0x01:0x00);
    }
    
    private static byte TEST(byte a) {
    	return (byte)(((a) != 0)?0x01:0x00);
    }
 
    private static byte function_bit()
    /****************************************************************************
    *                                                                           *
    * Description:                                                              *
    *   Computes the result of the non-linear function F2= f(t,s) for both      *
    *   oneway functions 1 and 2.                                               *
    *                                                                           *
    * Parameters: none                                                          *
    *                                                                           *
    * Return: Result of F2, either 0 or 1.                                      *
    *                                                                           *
    ****************************************************************************/
    {
    	byte F01_index;  /* Index to tables F0 and F1 */
    	byte F2_index;   /* Index to table F2 */

        F01_index  = GETBIT( t[0], 1 );
        F01_index <<= 1;
        F01_index |= GETBIT( t[0], 2 );
        F01_index <<= 1;
        F01_index |= GETBIT( t[0], 4 );
        F01_index <<= 1;
        F01_index |= GETBIT( t[0], 5 );
        F2_index  = (byte)(F0_table[ F01_index ] & (byte)0x01);

        F01_index  = GETBIT( t[1], 0 );
        F01_index <<= 1;
        F01_index |= GETBIT( t[1], 1 );
        F01_index <<= 1;
        F01_index |= GETBIT( t[1], 3 );
        F01_index <<= 1;
        F01_index |= GETBIT( t[1], 7 );
        F2_index |= (byte)(F1_table[ F01_index ] & (byte)0x02);

        F01_index  = GETBIT( s[1], 5 );
        F01_index <<= 1;
        F01_index |= GETBIT( s[0], 0 );
        F01_index <<= 1;
        F01_index |= GETBIT( s[0], 2 );
        F01_index <<= 1;
        F01_index |= GETBIT( s[0], 6 );
        F2_index |= (byte)(F1_table[ F01_index ] & (byte)0x04);

        F01_index  = GETBIT( s[2], 6 );
        F01_index <<= 1;
        F01_index |= GETBIT( s[1], 0 );
        F01_index <<= 1;
        F01_index |= GETBIT( s[1], 2 );
        F01_index <<= 1;
        F01_index |= GETBIT( s[1], 3 );
        F2_index |= (byte)(F1_table[ F01_index ] & (byte)0x08);

        F01_index  = GETBIT( s[3], 1 );
        F01_index <<= 1;
        F01_index |= GETBIT( s[3], 3 );
        F01_index <<= 1;
        F01_index |= GETBIT( s[3], 4 );
        F01_index <<= 1;
        F01_index |= GETBIT( s[2], 5 );
        F2_index |= (byte)(F0_table[ F01_index ] & (byte)0x10);

        return F2_table[ F2_index ];
    }
    
    private static void shift_reg( byte shift_bit )
    /****************************************************************************
    *                                                                           *
    * Description:                                                              *
    *   Performs the shift operation of t,s. Both registers are shifted left    *
    *   by one bit (with carry propagation from s to t), the feedback given by  *
    *   "shift_bit" is inserted into s.                                         *
    *                                                                           *
    * Parameters:                                                               *
    *   shift_bit: Bit to be shifted into the register, must be 0 or 1.         *
    *                                                                           *
    * Return: none                                                              *
    *                                                                           *
    ****************************************************************************/
    {
        t[0] <<= 1;
        t[0] |= GETBIT( t[1], 7 );

        t[1] <<= 1;
        t[1] |= GETBIT( s[0], 7 );

        s[0] <<= 1;
        s[0] |= GETBIT( s[1], 7 );

        s[1] <<= 1;
        s[1] |= GETBIT( s[2], 7 );

        s[2] <<= 1;
        s[2] |= GETBIT( s[3], 7 );

        s[3] <<= 1;
        s[3] |= shift_bit;
    }
    
    private static byte feed_back( )
    /****************************************************************************
    *                                                                           *
    * Description:                                                              *
    *   Calculates the feedback for oneway function 2 by EXORing the specified  *
    *   bits from the shift register t,s.                                       *
    *                                                                           *
    * Parameters: none                                                          *
    *                                                                           *
    * Return: Result of feedback calculation, either 0 or 1.                    *
    *                                                                           *
    ****************************************************************************/
    {
    	byte sum;

        /* Fetch the relevant bits from shift register, perform first EXOR: */
        sum = (byte)((t[0] & (byte)0xB3) ^ (t[1] & (byte)0x80) ^
              (s[0] & (byte)0x83) ^ (s[1] & (byte)0x22) ^
              (s[3] & (byte)0x73));

        /* EXOR all 8 bits of "sum" and return the result: */
        
        int int_sum = sum<0?sum+256:sum;
        return (byte)(exor_table[ int_sum % 16 ] ^ exor_table[ int_sum / 16 ]);
    }


    /****************************************************************************
    *                                                                           *
    * Description:                                                              *
    *   Performs the initialization phase of the HITAG2/HITAG2+ cryptographic   *
    *   algorithm. This function can be used to implement the cryptographic     *
    *   protocol in transponder mode (HITAG2/HITAG2+) and remote mode (HITAG2+) *
    *   Depending on the mode of operation, the initialization is done with     *
    *   different input parameters.                                             *
    *                                                                           *
    *   a) Transponder Mode:                                                    *
    *   The feedback registers are loaded with identifier and immobilizer       *
    *   secret key, and oneway function 1 is executed 32 times thereafter.      *
    *   The global variables ident and secretkey are used for identifier,       *
    *   immobilizer secret key, the random number is given as an input          *
    *   to the function.                                                        *
    *                                                                           *
    *   b) Remote Mode:                                                         *
    *   The feedback registers are loaded with identifier and remote            *
    *   secret key, and oneway function 1 is executed 32 times thereafter.      *
    *   The global variables HT2ident and HT2secretkey are used for identifier, *
    *   remote secret key. The sequence increment (28bit) and the command ID    *
    *   (4bit) are given together as 32bit input data block to the function.    *
    *                                                                           *
    *   The function outputs the initialized global shift registers t,s.        *
    *                                                                           *
    * Parameters:                                                               *
    *   addr_rand: Pointer to a memory area of 4 u8s that contains the*
    *              random number (transponder mode)                             *
    *              or sequence increment + command ID (remote mode)             *
    *              to be used for the initialization.                           *
    *                                                                           *
    * Àµ√˜:
    *       Oneway1œ‡µ±”⁄PCF7952º”√‹À„∑®÷–µƒ:∞—IDº”µΩÀ„∑®ºƒ¥Ê∆˜,∞—∏ﬂ¡ΩŒª√‹¬Îº”»Î*
            À„∑®ºƒ¥Ê∆˜,∞—RAM∫ÕµÕÀƒŒª√‹¬Î(EEPROM÷–)º”»ÎÀ„∑®ºƒ¥Ê∆˜.
    * Return: none                                                              *
    *                                                                           *
    ****************************************************************************/
    public static void Oneway1_ext(byte[] addr_rand, byte[] screct)
    {
    	byte bit_mask;  /* Used to fetch single bits of random/sec_key.*/
    	byte byte_cnt;  /* u8 counter for random/secret key.         */
    	byte fb;        /* Feedback bit for oneway function 1.         */

        /* Initialise oneway function 1 with identifier and parts of secret key */
        t[0]=addr_rand [0];
        t[1]=addr_rand [1];
        s[0]=addr_rand [2];
        s[1]=0;
        s[2]=screct[4];
        s[3]=screct[5];
        
        /* Perform 32 times oneway function 1 (nonlinear feedback) */
        byte_cnt = 0;
        bit_mask = (byte) 0x80; /* Setup bit mask: MSB of first u8 */
        do
        {
        	
//        	Log.e("Oneway1_ext", "-----byte_cnt---------bit_mask---------");
//            Log.e("Oneway1_ext", "-----"+byte_cnt+"---------"+bit_mask+"---------");
//            Log.e("Oneway1_ext", SRBleUtils.bytesToHexString(addr_rand));
//            Log.e("Oneway1_ext", SRBleUtils.bytesToHexString(screct));
//            Log.e("Oneway1_ext", "-----------------------");
        	
            /* One round of oneway function 1: */
            fb = (byte)(function_bit()
                 ^ TEST( (byte) ((screct[byte_cnt] ^ addr_rand[byte_cnt]) & bit_mask) ));
            shift_reg( fb );

            /* Advance to next bit of random number / secret key: */
            bit_mask = (byte) ((bit_mask & 0xff )>>> 1);
            if ( bit_mask == 0 )
            {
                bit_mask = (byte) 0x80;
                byte_cnt++;
            }
            
        }
        while (byte_cnt < 4);
    }

    public static void Oneway2( byte[] addr, int length )
    /****************************************************************************
    *                                                                           *
    * Description:                                                              *
    *   Performs the encryption respective decryption of a given data block     *
    *   by repeatedly executing oneway function 2 and Exclusive-Oring with the  *
    *   generated cipher bits. The computation is repeated for the number       *
    *   of bits as specified by "length". The global shift register contents    *
    *   after the initialization by Oneway1(), or the current contents after    *
    *   the last call of Oneway2() is used as start condition.                  *
    *                                                                           *
    * Parameters:                                                               *
    *   addr:   Pointer to a memory area that contains the data to be           *
    *           encrypted or decrypted.                                         *
    *   length: Number of bits to encrypt / decrypt
    * Àµ√˜:
    *       Oneway2œ‡µ±”⁄PCF7952º”√‹À„∑®÷–µƒCALL r_crypto_get                   *
    *                                                                           *
    * Return: none                                                              *
    *                                                                           *
    ****************************************************************************/
    {
    	byte bit_mask; /* Mask for current bit of data block. */
    	byte bitval;

        bit_mask = (byte) 0x80; /* Setup bit mask: MSB of first u8 */
        int position = 0;
        do
        {

//        	Log.e("Oneway2", "-----length---------bit_mask---------");
//            Log.e("Oneway2", "-----"+length+"---------"+bit_mask+"---------");
//            Log.e("Oneway2", SRBleUtils.bytesToHexString(addr));
//            Log.e("Oneway2", "-----------------------");
        	
            /* Calculate cipher bit and perform EXOR on data bit: */
            /* NOTE: Timing invariant implementation (uses multiplication). */
            /* NOTE: Possible performance degradation on other machines.    */
            bitval = (byte)((function_bit() ^ TEST( (byte) (addr[position] & bit_mask) )) * bit_mask);
            addr[position] = (byte)((addr[position] & (byte)~bit_mask) | bitval);

            shift_reg( feed_back() );

            bit_mask = (byte) ((bit_mask & 0xff )>>> 1);
            if ( bit_mask == 0 )
            {
                bit_mask=(byte) 0x80;
                position++;
            }

            length--;
        }
        while ( length>0 );
    }
}