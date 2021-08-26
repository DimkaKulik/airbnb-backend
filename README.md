# Airbnb

## Description

- Training web-application: service for rent and delivery of apartments.
- The main technology for backend part is java with spring boot framework.
- Other technologies: MySQL, Git.

## Technical information (features and progress)

**1. Authorization**

- [x] Registration/Signing in
  - [x] Using JWT token
  - [x] Using third party services (Google, Github, etc.)
- [x] Restriction logic
- [x] Authorization based on roles (role user, role admin)
- [ ] User email confirmation
  - [ ] Mailgun api

**2. Apartments offers**
 - [x] Creating, reading, updating, deleting (+ restriction logic)
 - [ ] Sorting and searching by filters
 - [ ] Use of third party services for maps
 - [ ] Use of third party api for currency converter
 
**3. Working with files**
  - [ ] Working with photos 
    - [ ] User avatars
    - [ ] Apartments offers photos


**4. Chat and notifications**
  - [ ] Chat based on websocket protocol
  - [ ] Chat based on xmpp protocol
  - [ ] Notifications

**5. Booking of apartments**
  - [ ] Working with calendar
    - [ ] Using third party services
    - [ ] Creating self calendar
  - [ ] Working with payment services
  - [ ] Booking history


**6. Admin panel**
  - [ ] Admin panel