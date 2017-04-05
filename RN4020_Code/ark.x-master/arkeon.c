/* 
 * File:   arkeon.c
 * Author: Mu & Leddy
 *
 * Created on November 16, 2014, 6:05 PM
 */

#include "config.h"

#include <xc.h>
#include <USART.h>
#include <string.h>
#include <stdlib.h>
#include <stdio.h>
#include <ctype.h>

#include "putgetc.h"

char mux_input[20];             //byte array for input from MUX
char bt_input[20];              //byte array for input from Bluetooth

volatile bit mux_event;         //bit variable to signal a receive event from the MUX
volatile bit bt_event;          //bit variable to signal a receive event from the Bluetooth

short sensorIndex = 0;          //Index variable for writing through to sensors on device

char sensorArray0[20];          //char array to store data from sensor1
char sensorArray1[20];          //char array to store data from sensor2
//char sensorArray2[20];          //char array to store data from sensor3
char writeSensorString[80];     //char array to store the whole write string to the Bluetooth.

volatile bit checking_for_sensors;
#define SENSOR_COUNT  3
char has_sensor[SENSOR_COUNT];

/* BLUETOOTH */
char
parse_input(char *bt_input)         //Parses input from the mobile app through bluetooth
{
    char cc = 1;                // WV,handles,value

    // hoping for two commas.

    char *tmp = bt_input;

    char comma_count = 0;

    //find the last comma - temporary - for getting the value of this one command
    while ( *tmp && ( *tmp != '\r' ) && ( *tmp != '\n' ) ) {
        if ( *tmp++ == ',' ) comma_count++;
        if ( comma_count == 2 ) break;
    }

    if ( strncmp(tmp,"10",2) == 0 ) {   //check the value on or off; String Number Compare, tmp variable, 10 value, compare first 2 values --> sets to 0 if it's true
        cc = 0;                         //Set cc variable to 1
    }

    return(cc);                         //Return cc as bt_input
}

void interrupt low_priority bt_handler(void)        //Handles interrupts on UART2
{
    if ( RCSTA2bits.OERR ) {
        RCSTA2bits.CREN = 0;
        __delay_ms(5);
        RCSTA2bits.CREN = 1;
    }

    if ( PIR3bits.RC2IF ) {
//        bluetooth_add_data();//Peripheral Interrupt (Flag); UART2 Receive Interupt Flag; 1 says UART2 Recive Buffer is full
        //  UART2_Read_Text(bt_input,20);                      //Read the UART2 Receive buffer, put into bt_input, 20 byte array
        bt_event=1;                                        //Set bt_event to 1 so we know data is holding
        PIR3bits.RC2IF = 0;                                //Reset interrupt flag back to 0
    }
}

/*DATA CONVERSION FROM SENSORS*/
char
hexlookup(char h)                                   //expects a 4 bit number
{
    if ( (h & 0xF0) != 0 ) return(0);               // safety check
    static char hextable[] = "0123456789ABCDEF";
    return(hextable[h]);
}

char
convert_char_hex(char c,unsigned char hi_lo)
{
    if ( hi_lo ) {
        return(hexlookup( (c & 0xF0) >> 4 ));       //masking the top bits and clearing the lower 4 bits of c. Then shifts the 4 bits down to make a 4 bit number
    } else {                                        //hex lookup uses that as an index into a character array
        return(hexlookup( (c & 0x0F) ));
    }
}

void
add_sensors_data(char *data, char *formatted_sensor_data,int max_data_size)
{
    for ( short j = 0; j < max_data_size; j++ ) formatted_sensor_data[j] = 0;  // put all zeros into buffer

    // find the decimal point if any.
    char *tmp = data;
    char *dst = &formatted_sensor_data[1];  // leave room up front for a count
    char *end = &formatted_sensor_data[max_data_size];

    char count = 0;
    // skip to the decimal if there is one & copy characters as you go.

    if ( *tmp == '.' ) {
         *dst++ = '0'; count = 1; tmp++;
    }
    while ( *tmp && ( *tmp != '\r' ) && ( *tmp != '\n' )  && ( *tmp != ',' ) && ( dst < end ) ) {  // skip comma for now.
        if ( *tmp == '.' ) { tmp++; break; }
        count++;
        *dst++ = *tmp++;
    }

    // count the digits in the mantissa if there is one & copy characters as you go.

    if ( count > 0 ) {

        if ( *tmp && ( *tmp != '\r' ) && ( *tmp != '\n' ) && ( dst < end ) ) {
            while ( *tmp && ( *tmp != '\r' ) && ( *tmp != '\n' ) && ( dst < end ) ) {
                *dst++ = *tmp++;
            }
        }

        //  put the size of the mantissa in as a hex representation.
        formatted_sensor_data[0] = convert_char_hex(count,0);

        if ( dst >= (end-3) ) {
            dst--;
            *dst = 0;
            dst--;
            dst--;
            dst--;
        }

        *dst++ = 'F';  // use out of bound (from decimal representation) to indicate end of number.
        *dst++ = '0';
    } else {
        *dst = '0';
    }

}

unsigned char known_data_pattern(char **data) {
    char *tmp = *data;
    while ( isspace(*tmp) && *tmp ) tmp++;
    if ( strncmp(tmp,"DO:",3) == 0 ) {
        tmp += 3;
        while ( isspace(*tmp) && *tmp ) tmp++;
        if ( isdigit(*tmp ) ) {
            *data = tmp;
            return(1);
        }
    }
    return(0);
}

/*SENSOR MUX HANDLER*/
volatile bit mux_error;
volatile char mux_input_index = 0;


//void interrupt mux_handler(void)
//{
//    if( ( PIE1bits.RC1IE == 1 ) && ( PIR1bits.RC1IF == 1 ) ) { //If we get an interrupt from the RX pin connected to the MUX; says receive buffer on UART1 is full
//
//        PIR1bits.RC1IF = 0;
//
//        if ( RCSTA1bits.OERR ) {
//            RCSTA1bits.CREN = 0;
//            __delay_ms(5);
//            RCSTA1bits.CREN = 1;
//            mux_error = 1;
//            return;
//        }
//
//        if ( RCSTA1bits.FERR ) {
//            RCREG1; RCREG1; RCREG1; RCREG1;
//            mux_error = 1;
//            return;
//        }
//
//
//        UART1_Read_Line(mux_input,20);                      //Read the UART1 receive BUFFER and store in mux_input
//
//        if ( !checking_for_sensors ) {
//            if ( isdigit(mux_input[0]) ) {
//                if ( sensorIndex == 0 ) {
//                    add_sensors_data(mux_input,sensorArray0,20);
//                }
//            } else return;
//        }
//
//        mux_event=1;                                        //set mux_event to 1 so we know data is holding
//
//    }
//}


volatile char mux_buffer_overflow = 0;

void init_mux_buffer(void) {
    for ( int i = 0; i < 20; i++ )  mux_input[i] = 0;
    mux_input_index = 0;
    mux_buffer_overflow = 0;
}


void interrupt mux_handler(void)
{
    if( ( PIE1bits.RC1IE == 1 ) && ( PIR1bits.RC1IF == 1 ) ) { //If we get an interrupt from the RX pin connected to the MUX; says receive buffer on UART1 is full

        PIR1bits.RC1IF = 0;

        if ( RCSTA1bits.OERR ) {
            RCSTA1bits.CREN = 0;
            __delay_ms(5);
            RCSTA1bits.CREN = 1;
            mux_error = 1;
            return;
        }

        if ( RCSTA1bits.FERR ) {
            RCREG1; RCREG1; RCREG1; RCREG1;
            mux_error = 1;
            return;
        }


        char c = UART1_Read();

        if ( !checking_for_sensors ) {

           if ( !mux_buffer_overflow ) {

               if ( isdigit(c) || ( c == ',' ) || ( c == '.' ) ||  ( c == '\r' ) ) {

                    mux_input_index++;

                    if ( ( c == '\r' ) || ( mux_input_index == 20 ) ) {

//                        mux_buffer_overflow = 1;

                        mux_input[ mux_input_index - 1 ] = 0;
                        mux_input_index = 0;

                        char *data = (char *)mux_input;

                            if ( sensorIndex == 0 ) {
                                add_sensors_data(data,sensorArray0,20);
                            } else if ( sensorIndex == 1 ) {
                                add_sensors_data(data,sensorArray1,20);
                            } else if ( sensorIndex == 2 ) {
//                                add_sensors_data(data,sensorArray2,20);
                            }

                        for ( int i = 0; i < 20; i++ ) mux_input[i] = 0;
                        mux_event=1;                                        //set mux_event to 1 so we know data is holding

                    } else {
                        mux_input[mux_input_index-1] = c;
                    }

               }

            }

        } else {
           mux_event=1;
        }

        //      UART1_Read_Line(mux_input,20);                      //Read the UART1 receive BUFFER and store in mux_input

    }
}


/*MUX Switch Channel Function*/
void MUXchannel (unsigned int channel)
{
    switch ( channel ) {
        case 0: {  //Sensor 1
            LATAbits.LATA6 = 0;     //Set S0 on MUX to low
            LATCbits.LATC0 = 0;     //Set S1 on MUX to low
            break;
        }
        case 1: {   //Sensor2
            LATAbits.LATA6 = 0;     //Set S0 on MUX to low
            LATCbits.LATC0 = 1;     //Set S1 on MUX to high
            break;
        }
        case 2: {
            LATAbits.LATA6 = 1;     //Set S0 on MUX to high
            LATCbits.LATC0 = 1;     //Set S1 on MUX to high
            break;
        }
    }
}

/*INITIALIZE PRIORITY INTERRUPTS, MUX HIGH PRIORITY, BLUETOOTH LOW PRIORITY*/
 void interrupt_init(void)
 {

     RCONbits.IPEN = 1;         //Reset Control Register; Interrupt Priority Enable Bit; 1 enables priority levels on interrupt
     INTCONbits.GIEH = 1;       //Global Interrupt Enable - High Priority
     INTCONbits.GIEL = 1;       //Glboal Interrupt Enable - Low Priority

//     RCONbits.IPEN = 0;         //Reset Control Register; Interrupt Priority Enable Bit; 0 disables priority levels on interrupts
//     INTCONbits.PEIE = 1;       //Interrupt Control Register, Peripheral Interrupt Enable Bit; 1 enables all unmaske peripheral interrupts
//     INTCONbits.GIE = 1;        //Interrupt Control Register, Global Interrupt Enable bit; 1 enables all unmasked interrupts when IPEN = 0
 }


 void soft_delay(short tt)
 {
     while ( tt > 0 ) {
        __delay_ms(25);                     //Delay 25 ms
        tt -= 25;                           //Subtract 25 from tt
     }
 }

// select MUX channel and request data from the sensor on that channel
void send_sensor_cmd(char sensor_index) {
    
    PIE1bits.RC1IE = 0;                 //Disable UART1 Receive Interrupt
    MUXchannel(sensor_index);           // set MUXchannel to switch index
    soft_delay(1000);
    PIE1bits.RC1IE = 1;                 //Enable UART1 Receive Interrupt

    UART1_Write_Char('R');              //Tells Atlas circuit to take one reading
    UART1_Write_Char(13);               //<CR>

    init_mux_buffer();
    soft_delay(3000);
}

//#define COUNT_UP_CHECK_LIMIT 10

//void
//check_for_sensors(void)
// {
//
//     short i;
//     short count_up = 0;
//     checking_for_sensors = 1;
//
//     for ( i = 0; i < SENSOR_COUNT; i++ ) {
//
//        has_sensor[i] = 0;
//
//        MUXchannel(i);          // set MUXchannel to switch index
//        soft_delay(100);
//        UART1_Write_Char('I');          //Tells Atlas circuit to take one reading
//        UART1_Write_Char(13);           //<CR>
//        soft_delay(100);
//
//        count_up = 0;
//        while ( !mux_event ) {
//            count_up++;
//            soft_delay(50);
//            if ( count_up > COUNT_UP_CHECK_LIMIT ) break;
//        }
//
//        if ( mux_event ) {
//             has_sensor[i] = 1;
//        }
//
//     }
// }

/*INITIALIZE PIC18F*/
void intialize(void)
{
    //Interrupt handlers -- initialize flags
    bt_event = 0;                           //Set the bt_event to 0
    mux_event = 0;                          //Set the mux_event to 0
    mux_error = 0;

//    checking_for_sensors = 0;
//    has_sensor[0] = 1;
//    has_sensor[1] = 1;
//    has_sensor[2] = 0;

    //Set Clock Speed
    OSCCON = 0xF0;                          //OSCCON set to 16MHz (0xF0=11110000 on the OSCCON register)
    OSCTUNEbits.PLLEN = 1;                  //Enable 4x multipler for 64MHz total clock speed

    //Disable analog peripherals
    ANSELA = 0;                             //disable Analag peripherals on PORT A
    ANSELC = 0;                             //disable Analog peripherals on PORT C

    //Clear Port value
    PORTA = 0;                              //clear values on PORTA
    PORTB = 0;                              //clear values on PORTB
    PORTC = 0;                              //clear values on PORTC

    //Setup Bluetooth I/O connections
    TRISAbits.TRISA1 = 0;                   //set RA1 to output (WAKE_SW on Bluetooth) *This can fluctuate
    TRISAbits.TRISA2 = 0;                   //set RA2 to output (WAKE_HW on Bluetooth) *This can fluctuate
    LATAbits.LATA1 = 1;                     //Set RA1 to high (WAKE_SW on Bluetooth)
    LATAbits.LATA2 = 0;                     //Set RA2 to high (WAKE_HW on Bluetooth)

    //Setup MUX I/O connections
    TRISAbits.TRISA6 = 0;                   //Set RA6 to output -- S0 on MUX
    TRISCbits.TRISC0 = 0;                   //Set RC8 to output -- S1 on MUX

    //Setup Priority Interrupts
    interrupt_init();                       //Initialize interrupts

    //Initialize UARTs
    USART1_init();                          //Initialize MUX UART
    soft_delay(1000);
    USART2_init();                          //Initialize Bluetooth UART
}


int main (void)
{
    short i;                                //Counter variable for MUX
    char inProcess = 1;                     //Char inProcess as a state machine of whether the sensor loop is running or not
    sensorIndex = i = 0;                    // Set sensorIndex counter, start at 0

    intialize();

    soft_delay(100);

/*Clear UART Bluetooth*/
//    while ( bluetooth_has_data() )  bluetooth_read_char();
//    bt_event = 0;

    send_sensor_cmd(0);

    while(1)
    {
        //MUX (sensor) event
//      if ( inProcess == 1 ) {
        if ( mux_event && (inProcess == 1) ) {                  //If mux_event is set to 1 from MUX Handler, and inProcess is set to 1 from a Bluetooth initialized byte

            mux_event = 0;                                      //Reset handler indicator; Set mux_event back to 0

            i = (++i) % 1;                                      // cycle 0,1,2

            if ( i == 0 ) {                                     //After a complete cycle, send through bluetooth; When i gets back to 0, showing a full cycle
                strcpy(writeSensorString,"shw,001c,");          //Bluetooth command, write to server; Start the sensor string with appropriate Bluetooth command and handle
                strcat(writeSensorString,sensorArray0);         //Arrays formatted during reading mux; Amend on first sensor
                strcat(writeSensorString,sensorArray1);         //Arrays formatted during reading mux; Amend on second sensor

                UART2_Write_Line((char *)writeSensorString);    //Write full sensor string to Bluetooth
            }

            sensorIndex = i;                                    //sensorIndex set to 1 on first go
            send_sensor_cmd(sensorIndex);

        }

        //Bluetooth event
//        if ( bt_event ) {                           //If bt_event is set to 1 due to data being held from the bt_handler
//            bt_event = 0;                           //Reset handler indicator
//
//            char cmd = parse_input(bt_input);       //Process command by parsing the bt_input into cmd variable, will be 0 or 1
//            switch ( cmd ) {
//                case 0: {
//                    inProcess = 0;                  //Stop responding to MUX events; If cmd is 0, set inProcess to 0 so nothing happens
//                    break;
//                }
//                case 1: {
//                    inProcess = 1;                  //Start responding to MUX events; If cmd is 1, set inProcess to 1 to start the sensor loop
//                    sensorIndex = i = 0;            //Start taking data from the first sensor; send sensor command activated on Port 0 on MUX
//                    send_sensor_cmd(0);
//                    break;
//                }
//                default: {
//                    //
//                }
//            }
//         }

    }

    return 0;
}




