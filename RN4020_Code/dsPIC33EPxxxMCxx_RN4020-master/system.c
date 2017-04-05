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


#include <stdint.h>          /* For uint16_t definition                       */
#include <stdbool.h>         /* For true/false definition                     */

#include "system.h"          /* variables/params used by system.c             */

/******************************************************************************/
/* System Level Functions                                                     */
/*                                                                            */
/* Custom oscillator configuration funtions, reset source evaluation          */
/* functions, and other non-peripheral microcontroller initialization         */
/* functions get placed in system.c.                                          */
/*                                                                            */
/******************************************************************************/

/* Refer to the device Family Reference Manual Oscillator section for
information about available oscillator configurations.  Typically
this would involve configuring the oscillator tuning register or clock
switching useing the compiler's __builtin_write_OSCCON functions.
Refer to the C Compiler for PIC24 MCUs and dsPIC DSCs User Guide in the
compiler installation directory /doc folder for documentation on the
__builtin functions.*/

/* TODO Add clock switching code if appropriate.  An example stub is below.   */
void ConfigureOscillator(void)
{
    /* Set PLL bits */
    /* Fosc = 70MHz; Fcy = 35MHz */
    PLLFBD = 12;
    CLKDIVbits.PLLPRE = 0b00;
    CLKDIVbits.PLLPOST = 0b00;
    
    /* Disable Watch Dog Timer */
    RCONbits.SWDTEN = 0;

    __builtin_write_OSCCONH(0x03);              /* Set to HS PPL */
    __builtin_write_OSCCONL(OSCCON || 0x01);    /* Request switch */

    while(OSCCONbits.COSC != 0b011);

    /* Wait for Clock switch to occur */
    /* Wait for PLL to lock, only if PLL is needed */
    while(OSCCONbits.LOCK != 1);

    /* Before we got the crystal: 
     * We are not switching clocks
     * But if we are, we can use PLL to get to 70MIPS. When setting up PLL,
     * we must switch to non-PLL before making the changes, and then switch
     * back to oscillator. 
     * FRC is apparently 7.37MHz
     */
        
}

