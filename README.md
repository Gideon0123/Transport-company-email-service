# Transport Company Email Service

A robust, asynchronous email microservice built with Spring Boot that handles email notifications for the Transport Company project. This service is designed as a microservice component of the larger [Transport-company-project](https://github.com/Gideon0123/Transport-company-project) ecosystem.

## Overview

The Transport Company Email Service provides a decoupled, event-driven approach to sending emails across the transport company system. It communicates with other microservices via message queues (RabbitMQ/AMQP) to receive email requests and processes them asynchronously without blocking other services.

## Key Features

- **Asynchronous Email Processing**: Handles email sending without blocking the caller
- **RabbitMQ Integration**: Receives email events from other microservices via AMQP
- **Retry Logic**: Built-in retry mechanism for failed email attempts
- **Spring Mail Support**: Seamless integration with JavaMailSender
- **JSON Message Processing**: Uses Jackson for robust JSON serialization/deserialization
- **Microservice Architecture**: Designed to run independently as part of a larger distributed system

## Technology Stack

### Core Framework
- **Java 21**: Latest LTS version for enhanced performance and language features
- **Spring Boot 4.0.6**: Modern application framework with auto-configuration

### Dependencies

#### Spring Boot Starters
- `spring-boot-starter-amqp` - RabbitMQ/AMQP messaging support
- `spring-boot-starter-mail` - Email sending capabilities
- `spring-boot-starter-webmvc` - Web MVC support for HTTP endpoints
- `spring-boot-starter-json` - JSON processing support

#### Additional Libraries
- **Lombok** - Reduces boilerplate code with annotations (e.g., @Data, @Getter, @Setter)
- **Jackson** - JSON serialization/deserialization
  - `jackson-databind` - Core data binding
  - `jackson-datatype-jsr310` - Java 8+ Date/Time API support
- **Spring Retry 2.0.12** - Automatic retry handling for transient failures

#### Testing Dependencies
- `spring-boot-starter-amqp-test` - AMQP testing utilities
- `spring-boot-starter-mail-test` - Email testing support
- `spring-boot-starter-webmvc-test` - Spring MVC testing framework

### Build Tools
- **Maven**: Project management and build automation
- **Maven Wrapper (mvnw)**: Ensures consistent Maven version across environments

## Prerequisites

- **Java 21** or higher
- **Maven** (or use the included Maven Wrapper `mvnw`)
- **RabbitMQ** running and accessible for message queue operations
- **SMTP Server** configured for email sending

## Project Structure

```
Transport-company-email-service/
├── src/
│   ├── main/
│   │   ├── java/                 # Java source code
│   │   └── resources/            # Application properties and configurations
│   └── test/                     # Unit and integration tests
├── .mvn/                         # Maven wrapper configuration
├── mvnw                          # Maven wrapper script (Unix/Linux/Mac)
├── mvnw.cmd                      # Maven wrapper script (Windows)
├── pom.xml                       # Maven project configuration
└── README.md                     # This file
```

## Installation & Setup

### 1. Clone the Repository

```bash
git clone https://github.com/Gideon0123/Transport-company-email-service.git
cd Transport-company-email-service
```

### 2. Configure Application Properties

Create or update `src/main/resources/application.properties` or `application.yml` with your configuration:

```properties
# RabbitMQ Configuration
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest

# Email Configuration (SMTP)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true

# Application Server
server.port=8080
```

### 3. Build the Project

Using Maven Wrapper (recommended):

```bash
# Linux/Mac
./mvnw clean build

# Windows
mvnw.cmd clean build
```

Or with Maven (if installed):

```bash
mvn clean install
```

### 4. Run the Application

Using Maven Wrapper:

```bash
# Linux/Mac
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

Or run the compiled JAR:

```bash
java -jar target/email-0.0.1-SNAPSHOT.jar
```

## Usage

### Message Queue Integration

This microservice listens to RabbitMQ for email requests from other services. To send an email, publish a message to the configured queue with a payload like:

```json
{
  "to": "recipient@example.com",
  "subject": "Shipment Update",
  "body": "Your shipment has been delivered",
  "timestamp": "2026-05-13T10:30:00Z"
}
```

### REST Endpoints

If configured with HTTP endpoints, you can send emails directly:

```bash
POST /api/email/send
Content-Type: application/json

{
  "to": "user@example.com",
  "subject": "Test Email",
  "body": "This is a test email"
}
```

## Configuration

### RabbitMQ

Configure queue names, exchange names, and routing keys in your application properties:

```properties
app.rabbitmq.email.queue=email-queue
app.rabbitmq.email.exchange=email-exchange
app.rabbitmq.email.routing-key=email.*
```

### Email Retry Policy

Configure retry attempts and delays:

```properties
spring.retry.max-attempts=3
spring.retry.delay=1000
```

### Logging

```properties
logging.level.root=INFO
logging.level.com.example=DEBUG
```

## Integration with Transport-company-project

This microservice is a component of the larger [Transport-company-project](https://github.com/Gideon0123/Transport-company-project). It receives events from other microservices via RabbitMQ and processes email notifications for:

- Order confirmations
- Shipment tracking updates
- Delivery notifications
- System alerts and notifications

Services in the Transport Company ecosystem communicate asynchronously through message queues, ensuring loose coupling and high scalability.

## Development

### Running Tests

```bash
./mvnw test
```

### Building for Production

```bash
./mvnw clean package -DskipTests
```

This creates an optimized JAR file in the `target/` directory.

## Troubleshooting

### RabbitMQ Connection Issues
- Ensure RabbitMQ is running on the configured host and port
- Verify credentials in `spring.rabbitmq.username` and `spring.rabbitmq.password`
- Check firewall rules for port 5672 (default RabbitMQ port)

### Email Sending Failures
- Verify SMTP server credentials and settings
- Check email provider's app password requirements (e.g., Gmail requires app-specific passwords)
- Ensure the SMTP port is not blocked by firewall
- Enable "Less secure app access" or use OAuth2 if required

### Build Issues
- Ensure Java 21+ is installed: `java -version`
- Clear Maven cache: `./mvnw clean`
- Check for conflicting versions in pom.xml

## Performance Considerations

- **Async Processing**: Emails are processed asynchronously to prevent blocking
- **Retry Logic**: Failed emails are automatically retried per configuration
- **Connection Pooling**: RabbitMQ and mail connections are pooled for efficiency
- **Scalability**: The service is stateless and can be scaled horizontally

## Notes

- **No Docker**: This project runs natively on the host system without containerization
- **Microservice Architecture**: Designed to run as an independent service alongside other microservices
- **Event-Driven**: Follows asynchronous, event-driven patterns for inter-service communication

## License

This project is part of the Transport Company project ecosystem.

## Support

For issues, questions, or contributions related to this microservice, please refer to the main [Transport-company-project](https://github.com/Gideon0123/Transport-company-project) repository or create an issue in this repository.

---

**Last Updated**: May 13, 2026
