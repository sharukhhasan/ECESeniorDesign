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
#include <stdbool.h>
#include <p33EP512MC502.h>         /* For true/false definition                     */
#include "user.h"            /* variables/params used by user.c               */

/******************************************************************************/
/* User Functions                                                             */
/******************************************************************************/

/* <Initialize variables in user.h and insert code for user algorithms.> */

void InitApp(void)
{
    /* TODO Initialize User Ports/Peripherals/Project here */

    /* Setup analog functionality and port direction */
    ANSELA = 0;
    ANSELB = 0;
    
    TRISA = 0;
    TRISB = 0;
    
    PORTA = 0;
    PORTB = 0;
    
    /* Initialize peripherals */
    
    /* Initializing timer */
    T1CONbits.TON = 0;  // Turn off the timer (if not already off)
    T1CONbits.TCS = 0;  // Internal clock
    T1CONbits.TGATE = 0; // Disable Gate mode
    T1CONbits.TCKPS = 0b11; // 1:256 prescale
    
    TMR1 = 0x0000;      // reset counter
    PR1 = 34179;        // Period of time per cycle
    
    // Enable the interrupt for the timer
    IPC0bits.T1IP = 0x01; // Set Timer 1 Interrupt Priority Level
    IFS0bits.T1IF = 0; // Clear the interrupt bit
    IEC0bits.T1IE = 1; // Enable the interrupt
    
    T1CONbits.TON = 1; // Turn on the timer
    
    TRISBbits.TRISB5 = 1; // Set button as output
    
}