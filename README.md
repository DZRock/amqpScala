Example of using AMQP scala client, activerecrd like ORM and scaldi DI provider.
To listen queue pls add fucntion to ru.dzdev.amqpScala.controller.RequestController and annotate this @QueryListener(queueName).
That example use RabbitMQ AMQP as data bus for exchange dat with client.
