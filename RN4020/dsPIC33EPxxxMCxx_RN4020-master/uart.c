/*
 * File:   uart.c
 * Author: Shanush
 *
 * Created on October 3, 2016, 8:17 AM
 */

/* Device header file */
#if defined(__XC16__)
    #include <xc.h>
#elif defined(__C30__)
    #if defined(__dsPIC33E__)
    	#include <p33Exxxx.h>
    #elif defined(__dsPIC33F__)
    	#include <p33Fxxxx.h>
    #endif
#endif

#include <stdint.h>        /* Includes uint16_t definition   */
#include <stdbool.h>       /* Includes true/false definition */
#include "uart.h"

#define LINES 5

int TX_data_available;
int TX_slot_available;
int TX_cons_index;
int TX_prod_index;
int RX_data_available;
int RX_slot_available;
int RX_cons_index;
int RX_prod_index;

char TX_buffer[LINES][CHARACTERS];
char RX_buffer[LINES][CHARACTERS];

int TX_current_character;   /* For keeping track of the character when sending */
int RX_current_character;   /* For keeping track of the character when receiving */


void uart_setup(void) {
    
    TX_data_available = 0;
    TX_slot_available = LINES;
    TX_cons_index = 0;
    TX_prod_index = 0;
    
    RX_data_available = 0;
    RX_slot_available = LINES;
    RX_cons_index = 0;
    RX_prod_index = 0;
    
    TX_current_character = 0;
    RX_current_character = 0;
    
    //------ UART -----------
    TRISBbits.TRISB10 = 1;  // Make UartRx pin as Input
    TRISBbits.TRISB11 = 0;  // Make UartTx pin as Output
    
    // Setting up PPS to use UART
    RPINR18bits.U1RXR = 0b0101010;      // Set Pin RP42 as U1RX
    RPOR4bits.RP43R = 0b000001;         // Set Pin RP43 as U1TX
    
    
    // Setting up UART
    U1MODEbits.UARTEN = 0;      // Disable UART during setup
    U1MODEbits.USIDL = 0;       // Continues module operation in Idle mode
    U1MODEbits.IREN = 0;        // IrDA encoder and decoder are disabled
    U1MODEbits.RTSMD = 1;       // UxRTS pin is in Simplex mode
    U1MODEbits.UEN = 0b00;      // UxTX and UxRX pins are enabled and used; UxCTS and UxRTS/BCLKx pins are controlled by PORT latches
    U1MODEbits.WAKE = 0;        // No wake up
    U1MODEbits.LPBACK = 0;      // No loop back
    U1MODEbits.ABAUD = 0;       // Baud rate measurement is disabled or completed
    
    U1MODEbits.URXINV = 0;      // Not sure what this is ...
    
    U1MODEbits.BRGH = 0;        // BRG generates 16 clocks per bit period (16x baud clock, Standard mode)
    U1MODEbits.PDSEL = 0b00;    // 8-bit data, no parity
    U1MODEbits.STSEL = 0;       // 1 stop bit
    
    U1BRG = 18;                 // ( (Clock Freq) / (16 * Baud Rate) ) - 1
                                // For Baud rate = 115200
                                // Clock Freq = 35Mhz
    
    U1STAbits.UTXISEL0 = 0;
    U1STAbits.UTXISEL1 = 1;     // Interrupt when a character is transferred to the Transmit Shift Register (TSR) and as a result, the transmit buffer becomes empty
    U1STAbits.UTXINV = 0;       // UxTX Idle state is ?1?
    U1STAbits.UTXBRK = 0;       // Sync Break transmission is disabled or completed
    
    U1STAbits.URXISEL = 0b00;   // Interrupt for when a character arrives
    U1STAbits.ADDEN = 0;        // Address Detect mode is disabled
    U1STAbits.OERR = 0;         // Receive buffer has not overflowed; clearing a previously set OERR bit (1 ? 0 transition) resets the receiver buffer and the UxRSR to the empty state
    
    // Interrupts for UART
    IEC0bits.U1RXIE = 1;
    IEC0bits.U1TXIE = 1;
    
    IPC2bits.U1RXIP = 0b100;
    IPC3bits.U1TXIP = 0b100;
    
    
    
    
    // Debugging UART
    TRISAbits.TRISA4 = 1;  // Make UartRx pin as Input
    TRISBbits.TRISB5 = 0;  // Make UartTx pin as Output
    
    // Setting up PPS to use UART
    RPINR19bits.U2RXR = 0b0010100;      // Set Pin RP42 as U2RX
    RPOR1bits.RP37R = 0b000011;         // Set Pin RP43 as U2TX
    
    U2MODEbits.UARTEN = 0;      // Disable UART during setup
    U2MODEbits.USIDL = 0;       // Continues module operation in Idle mode
    U2MODEbits.IREN = 0;        // IrDA encoder and decoder are disabled
    U2MODEbits.RTSMD = 1;       // UxRTS pin is in Simplex mode
    U2MODEbits.UEN = 0b00;      // UxTX and UxRX pins are enabled and used; UxCTS and UxRTS/BCLKx pins are controlled by PORT latches
    U2MODEbits.WAKE = 0;        // No wake up
    U2MODEbits.LPBACK = 0;      // No loop back
    U2MODEbits.ABAUD = 0;       // Baud rate measurement is disabled or completed
    
    U2MODEbits.URXINV = 0;      // Not sure what this is ...
    
    U2MODEbits.BRGH = 0;        // BRG generates 16 clocks per bit period (16x baud clock, Standard mode)
    U2MODEbits.PDSEL = 0b00;    // 8-bit data, no parity
    U2MODEbits.STSEL = 0;       // 1 stop bit
    
    U2BRG = 18;                 // ( (Clock Freq) / (16 * Baud Rate) ) - 1
                                // For Baud rate = 115200
                                // Clock Freq = 35Mhz
    
    U2STAbits.UTXISEL0 = 0;
    U2STAbits.UTXISEL1 = 1;     // Interrupt when a character is transferred to the Transmit Shift Register (TSR) and as a result, the transmit buffer becomes empty
    U2STAbits.UTXINV = 0;       // UxTX Idle state is ?1?
    U2STAbits.UTXBRK = 0;       // Sync Break transmission is disabled or completed
    
    U2STAbits.URXISEL = 0b00;   // Interrupt for when a character arrives
    U2STAbits.ADDEN = 0;        // Address Detect mode is disabled
    U2STAbits.OERR = 0;         // Receive buffer has not overflowed; clearing a previously set OERR bit (1 ? 0 transition) resets the receiver buffer and the UxRSR to the empty state
    
    // Interrupts for UART
    //IEC1bits.U2RXIE = 1;
    //IEC1bits.U2TXIE = 1;
    
    //IPC7bits.U2RXIP = 0b100;
    //IPC7bits.U2TXIP = 0b100;
    
    
    U1MODEbits.UARTEN = 1;      // Enable UART
    U1STAbits.UTXEN = 1;        // Transmission bit enabled
    
    U2MODEbits.UARTEN = 1;      // Enable UART
    U2STAbits.UTXEN = 1;        // Transmission bit enabled
}

/* THE RX consumer 
 * Assumes emptyString is CHARACTERS long
 */
bool uart1_recieveData(char *emptyString) {
    
    if (RX_data_available == 0) {
        return false;
    }
    
    //while (RX_data_available == 0);
    RX_data_available--;
    
    int i = 0;
    while (i < CHARACTERS && RX_buffer[RX_cons_index][i] != 0) {
        emptyString[i] = RX_buffer[RX_cons_index][i];
        i++;
    }
   
     
    /* Clear the line for later use */
    /*for (i = 0; i < CHARACTERS; i++) {
        RX_buffer[RX_cons_index][i] = 0;
    }*/
    
    RX_cons_index = (RX_cons_index+1)%LINES;
    
   
    
    RX_slot_available++;
    
    return true;
}



/* The TX producer */
void uart1_sendData(char *stringData) {
    
    /* Wait for slot to be available */
    while (TX_slot_available == 0);
    TX_slot_available--;
    
    /* Transfer data into buffer */
    int i = 0;
    while (i < CHARACTERS && stringData[i] != 0) {
        TX_buffer[TX_prod_index][i] = stringData[i];
        i++;
    }
    
    TX_prod_index = (TX_prod_index + 1) % LINES;
    
    TX_data_available++;
    
    
    /* If TX_data_available is 1, it was 0
     * Hence there was no TX activity before it.
     * So start the sending, by sending one character
     */
    if (TX_data_available == 1) {
        TX_current_character = 0;
        char tempChar = TX_buffer[TX_cons_index][TX_current_character];
        TX_current_character++;
        
        // An interrupt can occur right after this statement, so we make sure
        // there is nothing else to do because executing this statement.
        U1TXREG = tempChar;
        U2TXREG = tempChar;
        
    }
    
}

/* Interrupt Handlers */

/* The RX producer */
char previous_RX_character = 0;

void __attribute__((interrupt,auto_psv)) _U1RXInterrupt(void)
{
    char nextChar = U1RXREG;
    IFS0bits.U1RXIF = 0;
    
    if (nextChar != 0) {

        if (RX_current_character == 0) {
            /* First character, so need a new line */
            while (RX_slot_available == 0);
            RX_slot_available--;
        }

        RX_buffer[RX_prod_index][RX_current_character] = nextChar;
        if (nextChar == '\r' || nextChar == '\n' || nextChar == '!') {

            /* We must check if \r was already given before the \n
             * I.e. sometimes the string is "hello\r\n".
             * We know this by checking the previous_ RX_character.
             * 
             * Hence we check if we are not in a new slot - and if not, finish
             * off the current slot and move to the next slot for the next string.
             */
            
            if ((previous_RX_character != '\r') && (previous_RX_character != '\n')) {

                /* Finish off the string */
                RX_buffer[RX_prod_index][RX_current_character+1] = '\n';
                RX_buffer[RX_prod_index][RX_current_character+2] = 0;
                RX_prod_index = (RX_prod_index + 1)%LINES;
                RX_data_available++;   

            } else {
                // Unnecessarily took a slot
                RX_slot_available++;
            }

            RX_current_character = 0;

        } else {
            RX_current_character++;
        }

        previous_RX_character = nextChar;
    }
        
    
    U2TXREG = nextChar;
}

/* The Main TX consumer */
void __attribute__((interrupt,auto_psv)) _U1TXInterrupt(void)
{
    char nextChar = 0;
    IFS0bits.U1TXIF = 0;
    
    /* Check if lines are available to send 
     * (Includes the line we are currently sending)
     */
    if (TX_data_available != 0) {
        
        nextChar = TX_buffer[TX_cons_index][TX_current_character];
        
        /* Clear the byte for later use 
         * Do it as we send - to do less per interrupt
         */
        TX_buffer[TX_cons_index][TX_current_character] = 0;
        TX_current_character++;
        
        /* If end of line */
        if (nextChar == 0) {
            TX_data_available--;
            
            TX_cons_index = (TX_cons_index + 1) % LINES;
            
            TX_current_character = 0;
            
            TX_slot_available++;
        }
        
    }
    
    
    if (nextChar != 0) {
        U1TXREG = nextChar;
        U2TXREG = nextChar;
    }
}


void __attribute__((interrupt,auto_psv)) _U2RXInterrupt(void) 
{
    IFS1bits.U2RXIF = 0;
}

void __attribute__((interrupt,auto_psv)) _U2TXInterrupt(void)
{
    IFS1bits.U2TXIF = 0;
}