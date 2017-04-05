/* 
 * File:   
 * Author: 
 * Comments:
 * Revision history: 
 */

// This is a guard condition so that contents of this file are not included
// more than once.  
#ifndef UART_H
#define	UART_H

#define CHARACTERS 30

void uart_setup(void);
bool uart1_recieveData(char *emptyString);
void uart1_sendData(char *stringData);

#endif	/* UART_H */

