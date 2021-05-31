# Ping-Pong

Simple multiplayer game.

### Servers discovery protocol
- Client: When want to discover servers sends `DISCOVER` message.
- Servers: Reply with `OFFER <TCP-PORT> <SERVER-NICK>` to every `DISCOVER` message.

### Waiting in queue protocol
- Client: Client is joining to queue by simply connecting to TCP port.
- Server: Sends `POSITION <NEW-POSITION>` messages when position in queue is changing.

### Invitation protocol
- Server: When client is first in the queue server sends `INVITE` message.
- Client: Sends his nick.
- Server: Validates nick and reply with `NICK VALID` or `NICK INVALID` and disconnects.

### Gameplay server protocol
Server sends periodically binary messages with undermentioned fields.
- Server Y coordinate 
- Ball X coordinate
- Ball Y Coordinate
- Client points count
- Server points count
- Game status (0 - pending, 1 - won, 2 - lost)

### Gameplay client protocol
Client sends periodically his Y coordinate in binary messages.


