from bluetooth import *

server_socket = bluetooth.BluetoothSocket(bluetooth.RFCOMM)
port = server_socket
server_socket.bind(("", port))
server_socket.listen(1)

"""uuid = "00001111-0000-1000-8000-00805f9b34fb"
"""
uuid = "1e0ca4ea-299d-4335-93eb-27fcfe7fa848"

bluetooth.advertise_service(server_socket, "Echo Server",
							service_id = uuid,
							service_classes = [uuid, SERIAL_PORT_CLASS],
							profiles = [SERIAL_PORT_PROFILE])
														

def setupConnection():
	server_sock = BluetoothSocket(RMCOMM


def wait_for_connection():
    print "Waiting for connection in RFCOMM channel %d" % port

    client_socket, client_info = server_socket.accept()
    print "Accepted connection from ", client_info
    listen_for_data(client_socket)


def listen_for_data(client_socket):
    while True:
        try:
            data = client_socket.recv(1024)
            print "Received: %s" % data
            client_socket.send(data)

        except IOError:
            print "Client has disconnected"
            wait_for_connection()
            break

        except KeyboardInterrupt:
            print "Shutting down socket"
            client_socket.close()
            server_sock.close()


wait_for_connection()
