/******************************************************************************/
/* Files to Include                                                           */
/******************************************************************************/

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


#include <stdint.h>        /* Includes uint16_t definition                    */
#include <stdbool.h>       /* Includes true/false definition                  */
#include <stdio.h>         /* Includes sprintf */
#include <string.h>        


#include "system.h"        /* System funct/params, like osc/peripheral config */
#include "user.h"          /* User funct/params, such as InitApp              */
#include "uart.h"
#include "bluetooth.h"

/******************************************************************************/
/* Global Variable Declaration                                                */
/******************************************************************************/

/* i.e. uint16_t <variable_name>; */

/******************************************************************************/
/* Main Program                                                               */
/******************************************************************************/

int16_t main(void)
{
    
    /* Configure the oscillator for the device */
    ConfigureOscillator();

    /* Initialize IO ports and peripherals */
    InitApp();
    uart_setup();
    
    bool result;
    result = bluetooth_setup();
    
    if (result == true) {
        PORTBbits.RB12 = 1;
    } else {
        PORTBbits.RB12 = 0;
    }
    
    Nop();
    
    PORTBbits.RB14 = 1;
 
    bool endLoop = false;
    char messageBox[CHARACTERS] = {0};
    
    
    while(!endLoop)
    {
        
        next_message(messageBox);
        
        switch (messageBox[0]) {
            case 'D':
                endLoop = true;
                break;
            case 'M':
                if (messageBox[1] == 'R') {
                    PORTBbits.RB14 = ~PORTBbits.RB14;
                } else if (messageBox[1] == 'L') {
                    PORTBbits.RB12 = ~PORTBbits.RB12;
                } else if (messageBox[1] == 'X') {
                    PORTBbits.RB14 = 0;
                    Nop();
                    PORTBbits.RB12 = 0;
                    Nop();
                }
                break;
            default:
                break;
        }
    }
    
    bluetooth_shutdown();
    
    /* Wait a few seconds before fully shutting down */
    long x = 0;
    long y = 0;
    for (x = 0; x < 100000; x++) {
        for (y = 0; y < 50; y++) {
            Nop();
        }
    }
    
    return 0;
}
