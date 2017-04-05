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

/******************************************************************************/
/* Configuration Bits                                                         */
/*                                                                            */
/* This is not all available configuration bits for all dsPIC devices.        */
/* Refer to the dsPIC device specific .h file in the compiler                 */
/* support\dsPIC33F\h directory for complete options specific to the device   */
/* selected.  For additional information about what hardware configurations   */
/* mean in terms of device operation, refer to the device datasheet           */
/* 'Special Features' chapter.                                                */
/*                                                                            */
/* A feature of MPLAB X is the 'Generate Source Code to Output' utility in    */
/* the Configuration Bits window.  Under Window > PIC Memory Views >          */
/* Configuration Bits, a user controllable configuration bits window is       */
/* available to Generate Configuration Bits source code which the user can    */
/* paste into this project.                                                   */
/******************************************************************************/

/* TODO Fill in your configuration bits from the config bits generator here.  */

#pragma config GCP=OFF
#pragma config GWRP=OFF
#pragma config IESO=OFF
#pragma config PWMLOCK=OFF
#pragma config FNOSC=FRC
#pragma config FCKSM=CSECMD
#pragma config IOL1WAY=OFF
#pragma config OSCIOFNC=ON
#pragma config POSCMD=HS
#pragma config FWDTEN=OFF
#pragma config WINDIS=OFF
#pragma config PLLKEN=ON
#pragma config WDTPRE=PR128
#pragma config WDTPOST=PS32768
#pragma config WDTWIN=WIN50
#pragma config ALTI2C1=OFF
#pragma config ALTI2C2=OFF
#pragma config JTAGEN=OFF
#pragma config ICS=PGD1
