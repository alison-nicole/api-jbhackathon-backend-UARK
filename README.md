# Backend Readme

# Application Purpose

This application serves as the API for the Hackathon hosted by J.B. Hunt. It provides functionalities that center around four core ideas:

- Users should be able to register for an upcoming Hackathon (Participant)
- Users should be able to create and manage teams (Team)
- Users should be able to judge teams (Judge)
- Users should be able to manage Hackathons (Admin)

## How to Run the Application

- [ ]  Java 11
- [ ]  Maven 3.6.3
- [ ]  Add necessary environment variables - values provided by devs upon request
    - [ ]  username
    - [ ]  password
    - [ ]  email-apikey

# Current State of the Application

The current application allows for the manipulation of data stored in the database so that the responsibilities of users can be met. As of right now, core functionality only exists for Participants and Judges. Right now, there are no protected endpoints - all endpoints are front facing and can be hit by anyone. This is a problem and will be addressed.

## Security

Since we are off of the J.B Hunt platform, we had to get creative with the security system.

The entire system is built fundamentally around JWT tokens. At it's core, the security system is a complex JWT reader and writer.

To abstract away the complexities of reading/writing JWT tokens, there exists a class called 'AuthorizedUser'. This class is nothing more than a collection of authorities and a set of functions which work with the JwtTokenUtil class, but it also makes up the core of our security. It has functions which allow it to validate tokens, generate tokens, build tokens, and create instances of AuthorizedUsers from tokens. This allows future programmers to interact with AuthorizedUser instances rather than having to interact directly with the JwtTokenUtil class, which can be annoying and messy.

The AuthorizedUser class itself is abstract. To extend it, a function which returns a JwtTokenBuilder, toTokenBuilder, must be implemented. This might seem weird, but it allows future extensions of the AuthorizedUser class to customize how the JWT tokens are produced. However, full control is still not given to the AuthorizedUser class extension, as there are some things which must follow convention. The issuedAt date, expirationDate, and signingKey will **always** be set accurately on generated JWT tokens, regardless of what the AuthorizedUser class extension might dictate. Further functions which can be overriden are 'validate' and 'fromToken'. The validate function returns a boolean value indicating whether or not a given token is valid in the context of the AuthorizedUser. The fromToken function converts a token to an AuthorizedUser instance.

Authorized user extension classes must also implement an init method. The init method is called regardless of how or where the instance was created. While anything can be done in this method, it is a good idea to grant any default authorities here.

There are two base AuthorizedUser extension classes in the current system: AnonymousUser and IdentifiedUser. These are two very basic classes, and they both represent exactly what they sound like. AnonymousUser's are simply users who the server does not know the identity of, while IdentifiedUser's are user's who's ID is known (and stored).

I've also implemented a JudgeUser to demonstrate how new user types can easily be created. You can see from this example that very little code is required to setup new authority groups.

When any request is made to the server, the first thing the server will do is check for a JWT token stored as a cookie on the request. If none is found, the server creates a new AnonymousUser, and stores the new user's token as a cookie on the response. Note that the ANONYMOUS_AUTHORITY_GROUP is automatically applied when the user is created here.

Inversely, if a token is found, the server will extract the AuthorizedUser class from the token and, using the fromToken method, generate an IdentifiedUser class instance with the appropriate user ID.

After the server has an appropriate AuthorizedUser instance created for the request, (be it identified or anonymous), spring will create authentication matching the authorities of the user. If the requested endpoint is within the user's authorities, the request is authenticated.

The majority of this logic is occurring within the JwtTokenCookieFilter.

Now, you might be asking: how can we generate tokens for an identified user in the first place if all requests are automatically anonymous?

That brings us to the last piece of the puzzle: verification. As of right now, to authenticate as a non anonymous user, a verification endpoint must be hit with a request which contains the name and email of the user being verified. Importantly, the verification endpoint must be different depending on what type of user is being verified (judge, participant, etc...). This is a bit annoying as it requires writing new code for each new type of user, but it is a consequence of the current database design. Still, it does allow for the roles to be added easily. After hitting the verification endpoint, the server will store a secure, http only cookie with the code for 60 seconds. All that must happen next is the '/auth/authenticate' must be hit with the correct code. If it is correct, the user will be authenticated and a corresponding token will be saved as a cookie.

### **Extending the current system:**

- Authorities: To add new authorities, simply add new types to the Authority enum and follow the naming convention of {CONTROLLER-NAME}_{AUTHORITY_DESC}.
- AuthorizedUser: For a very low level implementation, simply extend the AuthorizedUser class and implement all the required methods. However, this likely is not what you want to do. For the vast majority of situations, it is likely a good idea to extend the IdentifiedUser class. This gives you an id to work, and will automatically be created for you in the filter. If you don't need an ID, you could also override the AnonymousUser class (which will also be created automatically). If your implementation logically inherits authorities from another AuthorizedUser implementation, you should extend the other AuthorizedUser implementation. This prevents the need to write duplicate code. An example of this could be a JudgeUser inheriting from a ParticipantUser. In this case, the JudgeUser would automatically inherit all authorities that the ParticipantUser has, and allow more to be easily added. Further, if a Judge does not need some authority which a participant has, the revoke method can be used.
- Authentication Controller Verification Endpoints: This is how you control what type of user instance is created. You do this by creating an AuthorizedUser inheritor instance. To create an endpoint, simply add a new post mapping on the AuthenticationController with the name verify{USER_TYPE}. The method should accept a VerificationRequestDTO and an HttpServletResponse. The main goal of this function is to create some AuthorizedUser and then interact with the VerificationService to create and store a verification cookie. For examples of this, see the verifyJudge method in the AuthenticationController.

# Future State of the Application

The core philosophy of the application is going to be changed - centered around the new User object. The User object will allow for cross-hackathon user management and will integrate nicely with Auth0.

The application is going to move away from the implemented security system because of a couple critical flaws. Instead, the application will integrate with Auth0 who will become the authentication/authorization provider. This will allow us to guard our endpoints from unauthorized users, providing better security to our application’s structure.

Much of the database will change to better follow the User philosophy. In response, much of the backend will be removed or refactored.
This application is going to go under a massive rework.