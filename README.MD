
# ISW SMART TILL SDK Technical Documentation

To start using the SDK, follow the steps below:
- add module `isw-smart-till-release`  to your project

```Kotlin  

TillBluetoothManager(<context>, <TillCallBackListener>)

```  

## Bluetooth Manager Methods

#### Start Service
Before calling other methods the service must be started first

```Kotlin  

<managerService>.startService()

```  

#### Sending Command
To send a command

```Kotlin  

<managerService>.sendCommand(command:TillCommand)

``` 


#### Till Call Back Listener
In order to get the response from the terminal
```Kotlin
onMessageReceived(message: String) // Returns a message
onCommandReceived(command: TillCommand) // returns a command sent from the terminal
onConnected(device: String) // when the divice successfully connects, returns the device name
onError(error: TillReturnCodes, message: String?) // When an error occurs
onDisConnected() // When the connection is disconnected

```
  