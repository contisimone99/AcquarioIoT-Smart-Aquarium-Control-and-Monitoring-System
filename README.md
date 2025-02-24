# AcquarioIoT: Smart Aquarium Control and Monitoring System

## Project Overview

AcquarioIoT is a comprehensive IoT-based system for controlling and monitoring aquarium environments. This project utilizes MQTT messaging for communication between components, includes data archiving capabilities, and provides a web interface for monitoring and control.

## System Architecture

The system consists of several components:

- **AcquarioControllo**: Main control system for aquarium hardware interfaces
- **ArchiverAcquario**: Data archiving service for storing sensor readings and events
- **CloudAppManager**: Web server providing REST API and user interface
- **DomainManager**: MQTT domain and user management component
- **LibServer**: Utility library for common functions

## Technologies Used

- Java 8-15 (varies by component)
- MQTT for messaging (Eclipse Paho client)
- JSON for data interchange
- SQLite for local data storage
- REST API for web interface
- BeagleBone for hardware interface

## Setup and Configuration

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- Eclipse IDE (recommended for development)
- MQTT broker (e.g., Mosquitto)
- BeagleBone board for hardware interface

### Configuration Files

The system uses JSON configuration files:

```json
{
  "domain": "gruppo3",
  "services": [
    {"service":"archiver", "host":"cloud", "uri":"localhost:3000/lib/files/Archiver.jar"},
    {"service":"netdevices", "host":"beaglebone", "uri":"localhost:3000/lib/files/Control.jar"}
  ],
  "users": [
    {"username":"gruppo3", "role":"A", "password":"1a1126a4e8e4c866469c45c4a15e70afaf1639135babd84ae0424693688e0e9f"},
    {"username":"pippo", "role":"U", "password":"1a1126a4e8e4c866469c45c4a15e70afaf1639135babd84ae0424693688e0e9f"}
  ],
  "topics": [
    {"role":"A", "topic": "+/gruppo3/#"},
    {"role":"U", "topic":"from/gruppo3/acquario/#"},
    {"role":"U", "topic":"to/gruppo3/acquario/#"}
  ]
}
```

## MQTT Topic Structure

The system uses a hierarchical topic structure:

- `from/[domain]/[device]/[sensor]`: Data published from devices
- `to/[domain]/[device]/[actuator]`: Commands sent to devices
- `rpc/[domain]/[device]/[service]`: Remote procedure calls

## Component Details

### AcquarioControllo

Controls the aquarium hardware, reads sensors, and publishes data to the MQTT broker.

### ArchiverAcquario

Archives sensor data and events for historical analysis and monitoring.

### CloudAppManager

Provides a web interface for monitoring and controlling the aquarium system.

### DomainManager

Manages MQTT domains, users, and access control for the system.

## Building and Deployment

Each component can be built using Eclipse or command-line Java tools:

```bash
# Example build command
javac -cp "lib/*" -d bin src/*.java

# Example run command
java -cp "bin:lib/*" RESTHttpServer
```

## Security

The system implements security through:

- User authentication with SHA-256 password hashing
- Role-based access control for MQTT topics
- TLS support for secure communication

## License

This project is part of the Pervasive Systems course laboratory implementation.
