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

#include <stdio.h>
#include <stdint.h>        /* Includes uint16_t definition   */
#include <stdbool.h>       /* Includes true/false definition */
#include <string.h>
#include "uart.h"
#include "bluetooth.h"

char wait_response(char *expected);
bool response_cmp(char *response, char *expected);
void delay();

/* Assumes UART setup is done */
bool bluetooth_setup(void) {
    
    int i = 0;
    
    char messageBox[CHARACTERS] = {0};
    
    TRISAbits.TRISA1 = 0; // Wake as output
    TRISAbits.TRISA0 = 0; // CMD as output
    
    PORTAbits.RA1 = 0; // Set low to start off with
    PORTAbits.RA0 = 0; 
    
    // Clear out any messages
    while (uart1_recieveData(messageBox));
    
    /* Wait a few seconds before initialising bluetooth*/
    long x = 0;
    long y = 0;
    for (x = 0; x < 100000; x++) {
        for (y = 0; y < 50; y++) {
            Nop();
        }
    }
    
    PORTAbits.RA1 = 1; // Wake up
    
    /* Wait for CMD signal */
    Nop();
    
    if (!wait_response("CMD")) {
        return false;
    }
    
    delay();
    
    /* Software reset */
    sprintf(messageBox, "SF,2\r\n");
    uart1_sendData(messageBox);
    
    if (!wait_response("AOK")) {
        return false;
    }
        
    delay();
    
    /* Set SR value */
    
    // Set SR
    unsigned long SR_value = AUTO_ADVERTISE | ENABLE_MLDP | AUTO_MLDP_DISABLE |
                   NO_DIRECT_ADVERTISEMENT | IOS_MODE;
    sprintf(messageBox, "SR,%08lX\r\n", SR_value);
    uart1_sendData(messageBox);
    
    if (!wait_response("AOK")) {
        return false;
    }
    
    delay();
    
    // Reboot
    for (i = 0; i < CHARACTERS; i++) {
        messageBox[i] = 0;
    }
    sprintf(messageBox,"R,1\r\n");
    uart1_sendData(messageBox);
    
    while (response_cmp(messageBox, "CMD") == false) {
        if (next_message(messageBox) == ERR) {
            return false;
        }   
    }
    
    Nop();
    
    delay();
    
    sprintf(messageBox, "GR\r\n");
    uart1_sendData(messageBox);
    
    if (!wait_response("3C004000")) {
        return false;
    }
    
    delay();
    
    /* Wait for Connected */
    if (!wait_response("Connected")) {
        return false;
    }
    
    delay();
    
    PORTAbits.RA0 = 1; // MLDP mode
    
    if (!wait_response("MLDP")) {
        return false;
    }
    
    return true;
}


bool bluetooth_shutdown(void) {
    
    PORTAbits.RA0 = 0; // CMD mode
    
    if (!wait_response("CMD")) {
        return false;
    }
    
    char messageBox[CHARACTERS] = {0};
    
    sprintf(messageBox, "K\r\n");
    uart1_sendData(messageBox);
    
    if (!wait_response("Connection End")) {
        return false;
    }
    
    delay();
    
    PORTAbits.RA1 = 0;

    if (!wait_response("END")) {
        return false;
    }
    
    return true;
}

char wait_response(char *expected) {
    
    char messageBox[CHARACTERS] = {0};
    
    while (response_cmp(messageBox, expected) == false) {
        if (next_message(messageBox) == ERR) {
            return FAILED;
        }
    }
    
    return SUCCESS;
}


char next_message(char *messageBox) {
    int i = 0;
    // Clear and receive acknowledgement
    for (i = 0; i < CHARACTERS; i++) {
        messageBox[i] = 0;
    }
    while(uart1_recieveData(messageBox) == false) Nop();
    
    if (response_cmp(messageBox, "ERR") == true) {
        return ERR;
    }
    
    return SUCCESS;
}

/* Checks the given response with expected response */
bool response_cmp(char *response, char *expected) {
    
    int i = 0;
    for (i = 0; i < strlen(expected)-1; i++) {
        if (response[i] != expected[i]) {
            return false;
        }
    }
    
    return true;
}

void delay() {
    long x = 0;
    long y = 0;
    for (x = 0; x < 100; x++) {
       for (y = 0; y < 50; y++) {
           Nop();
       } 
    }
}