NeedleDrop is a full-stack music library application built with **Spring Boot**, **PostgreSQL**, and **Next.js**.

The project focuses on backend architecture, authentication, and clean API design, with a lightweight frontend that consumes the API and demonstrates real integration.

Tech Stack:
Backend:
- Java 17
- Spring Boot
- Spring Security + JWT authentication
- PostgreSQL
- JPA / Hibernate
- Docker & docker-compose

Frontend:
- Next.js (App Router)
- TypeScript
- Tailwind CSS
- REST API integration

Core Functionality:

Implemented (Core Domain):
- User
- Album
- Song

They support:
- CRUD operations
- ownership and permission checks
- JWT-based authentication
- PostgreSQL persistence

Planned / In Progress:
The following features are partially scaffolded or planned for future development:

- Playlists
- Reviews
- Extended discovery & recommendation logic
- Enhanced frontend UI/UX

These are intentionally left unfinished to keep the project focused and maintainable.

Authentication:

- Stateless authentication using JWT
- Login and registration endpoints provided by the backend
- Tokens stored client-side and attached to API requests
- Role-based access control (user / admin)

Design Notes:
Backend is the primary focus of this project.
Frontend is intentionally minimal and API-driven.
Code prioritizes clarity, separation of concerns, and realistic architecture.
Not all features are implemented.

NeedleDrop was built to practice and demonstrate: real backend security patterns, database-backed domain modeling.

Author:
Veronika Rimkevičiūtė.

