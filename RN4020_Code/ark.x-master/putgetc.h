/* 
 * File:   putgetc.h
 * Author: Mu
 *
 * Created on November 16, 2014, 6:17 PM
 */

#ifndef PUTGETC_H
#define	PUTGETC_H

#ifdef	__cplusplus
extern "C" {
#endif



char  getch(void);
void  putch(char c);

void USART1_init(void);
void USART2_init(void);


/*Bluetooth UART Functions*/
void UART2_Write_Char(char data);

void UART2_Write_String(char *s);

void UART2_Write_Line(char *s);

char UART2_TX_Empty();

char UART2_Data_Ready();

char UART2_Read();

void UART2_Read_Text (char *Output, unsigned int length);


char bluetooth_read_char(void);
char bluetooth_has_data(void);
void bluetooth_add_data(void);


/*MUX UART Functions*/
void UART1_Write_Char(char data);

void UART1_Write_String(char *s);

char UART1_TX_Empty();

char UART1_Data_Ready();

char UART1_Read();

void UART1_Read_Text (char *Output, unsigned int length);

void UART1_Read_Line (char *Output, unsigned int length);




#ifdef	__cplusplus
}
#endif

#endif	/* PUTGETC_H */

