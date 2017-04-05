/* 
 * File:   putgetc.c
 * Author: penthouse
 *
 * Created on November 16, 2014, 6:18 PM
 */

#include <xc.h>
#include <USART.h>
#include <string.h>
#include "config.h"
#include "putgetc.h"


void USART2_init(void)       //Bluetooth UART connection
{
    TXSTA2bits.TXEN = 0;     //Set to 0 first to clear
    TXSTA2bits.TXEN = 1;     //Transmit Enabled
    TXSTA2bits.BRGH = 1;     //High Baud Rate
    TXSTA2bits.TX9 = 0;      //8-bit Transmition
    TXSTA2bits.SYNC = 0;     //Asynchronous Mode

    RCSTA2bits.CREN = 0;     //Set to 0 first to clear
    RCSTA2bits.CREN = 1;     //Enables Receive
    RCSTA2bits.RX9 = 0;      //8-Bit Reception

    TRISBbits.TRISB7 = 1;    //Set RB7 (RX2) to input -- TX on Bluetooth
    TRISBbits.TRISB6 = 1;    //Set RB6 (TX2) to input -- RX on Bluetooth

    SPBRG2 = 34;             //SPBRGHx:SPBRGx decimal; FOSC of 64MHz; 115200 desired Baud; BRGH=1
    SPBRGH2 = 0;
    BAUDCON2bits.BRG16 = 0;  //8-bit baud rate generator

    PIE3bits.RC2IE = 0;      //Enable UART2 Receive Interrupt?
//    IPR3bits.RC2IP = 0;      //UART2 Receive -> Low Priority

    RCSTA2bits.SPEN = 1;     //Serial Port 2 Enabled
}

void USART1_init(void)       //MUX UART connection
{
    TXSTA1bits.TXEN = 0;     //Set to 0 first to clear
    TXSTA1bits.TXEN = 1;     //Transmit Enabled
    TXSTA1bits.BRGH = 1;     //High Baud Rate
    TXSTA1bits.TX9 = 0;      //8-bit Transmition
    TXSTA1bits.SYNC = 0;     //Asynchronous Mode

    RCSTA1bits.CREN = 0;     //Set to 0 first to clear
    RCSTA1bits.CREN = 1;     //Enables Receive
    RCSTA1bits.RX9 = 0;      //8-Bit Reception

    TRISCbits.TRISC6 = 1;    //Set RC6 (TX1) to input -- RX (1Z) on MUX
    TRISCbits.TRISC7 = 1;    //Set RC7 (RX1) to input -- TX (2Z) on MUX

    SPBRG1 = 103;            //SPBRGHx:SPBRGx decimal; FOSC of 64MHz; 38400 desired Baud; BRGH=1
    SPBRGH1 = 0;
    BAUDCON1bits.BRG16 = 0;  //16-bit baud rate generator

    PIE1bits.RC1IE = 1;      //Enable UART1 Receive Interrupt
//    IPR1bits.RC1IP = 1;      //UART1 Receive -> High Priority

    RCSTA1bits.SPEN = 1;     //Serial Port 1 Enabled
}


/*Bluetooth UART Functions*/
void UART2_Write_Char(char data)
{
    while (!TXSTA2bits.TRMT);       //wait until transmit shift register is empty
    TXREG2 = data;                  //write character to TXREG and start transmission
}

void UART2_Write_String(char *s)
{
    while (*s)
    {
        UART2_Write_Char(*s);            //send character pointed to by s
        s++;                              //increase pointer location to the next character
    }
}


void UART2_Write_Line(char *s)
{
    UART2_Write_String(s);
    UART2_Write_Char('\r');

}


char UART2_TX_Empty()
{
    return TXSTA2bits.TRMT; //Returns Transmit Shift Status bit
}

char UART2_Data_Ready()
{
    return PIE3bits.RC2IE;      //Is the data ready to read from the Receive Register
}

char UART2_Read()
{
    while(!PIE3bits.RC2IE);     //Waits for Recpetion to complete
    return RCREG2;              //Returns the 8 bit data
}

void UART2_Read_Text (char *Output, unsigned int length)
{
    int i;
    for(int i=0; i<length;i++)
        Output[i] = UART2_Read();
}

/*MUX UART Functions*/
void UART1_Write_Char(char data)
{
    while (!TXSTA1bits.TRMT);
    TXREG1 = data;
}

void UART1_Write_String(char *s)
{
    while (*s)
    {
        UART1_Write_Char(*s);            //send character pointed to by s
        s++;                        //increase pointer location to the next character
    }
}

char UART1_TX_Empty()
    {
        return TXSTA1bits.TRMT; //Returns Transmit Shift Status bit
    }

char UART1_Data_Ready()
{
    return PIE1bits.RC1IE;      //Is the data ready to read from the Receive Register
}

char UART1_Read()
{
    while(!PIE1bits.RC1IE);     //Waits for Recpetion to complete
    return RCREG1;              //Returns the 8 bit data
}

void UART1_Read_Text (char *Output, unsigned int length)
{
    for(int i=0; i<length;i++)
        Output[i] = UART1_Read(); //Reads a desired length of text continuously
}


void UART1_Read_Line (char *Output, unsigned int length)
{

    for ( int j = 0; j < length; j++ ) {
        Output[j] = 0;
    }
    for ( int i=0; i < length-1; i++ ) {
        char c = UART1_Read();
        if ( c == '\r' ) break;
        Output[i] = c;
    }

//    UART1_Read();
//    Output[length] = 0;
    //for ( int k = 0; k < 20; k++ ) UART1_Read();
}




/*
 * Function: getch
 * Weak implementation.  User implementation may be required
 */

char
getch(void)
{
	return 0;
}

/*
 * Function: putch
 * Weak implementation.  User implementation may be required
 */

void
putch(char c)
{
}

