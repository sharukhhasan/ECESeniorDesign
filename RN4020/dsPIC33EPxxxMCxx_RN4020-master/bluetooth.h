/* 
 * File:   
 * Author: 
 * Comments:
 * Revision history: 
 */

// This is a guard condition so that contents of this file are not included
// more than once.  
#ifndef BLUETOOTH_H
#define	BLUETOOTH_H

#include <xc.h> // include processor files - each processor file is guarded.  

/* SR Features */
#define CENTRAL 0x80000000
#define REAL_TIME_READ 0x40000000
#define AUTO_ADVERTISE 0x20000000
#define ENABLE_MLDP 0x10000000
#define AUTO_MLDP_DISABLE 0x08000000
#define NO_DIRECT_ADVERTISEMENT 0x04000000
#define UART_FLOW_CONTROL 0x02000000
#define RUN_SCRIPT_AFTER_POWER_ON 0x01000000
#define ENABLE_AUTHENTICATION 0x00400000
#define ENABLE_REMOTE_COMMAND 0x00200000
#define DO_NOT_SAVE_BONDING 0x00100000
#define IO_CAPABILITIES 0x000E0000
#define BLOCK_SET_COMMANDS 0x00010000
#define ENABLE_OTA 0x00008000
#define IOS_MODE 0x00004000
#define SERVER_ONLY 0x00002000
#define ENABLE_UART_IN_SCRIPT 0x00001000
#define AUTO_ENTER_MLDP_MODE 0x00000800
#define MLDP_WITHOUT_STATUS 0x00000400


bool bluetooth_setup(void);
bool bluetooth_shutdown(void);

/* Get the next line from Bluetooth */
#define SUCCESS 1
#define NO_MESSAGE 0
#define ERR -1
#define FAILED 0

char next_message(char *messageBox);


#endif	/* BLUETOOTH_H */

