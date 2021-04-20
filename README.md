<img alt="MySQL Workbench Icon" src="resources/img/mysql-workbench-icon.png" src="src/main/resources/static/images/readme-icon.png" align="right" height=150 />

# :fire: Data Migration Project ![sparta_badge][sparta_badge] :fire:

> Data Migration Project for Sparta Global

## Table of Contents

#### [About :question:](#about)
#### [Dependencies :computer:](#dependencies)
#### [MVC Architecture :tokyo_tower:](#mvc)
#### [Threading :tshirt:](#threading)
#### [Performance :performing_arts:](#performance)
#### [DAO & DTO :clipboard:](#DAODTO)
#### [Future Enhancements :customs:](#future)

<div id='about'/>

## About :question:

As part of my training with Sparta Global, I was assigned the task of reading a CSV file and writing it to a SQL database. The CSV file contained a employee details, one a sample of 10,000 entries, the other being 65,000 rows long.

MySQL Workbench and Server were used to monitor and run the database locally, hence the use of the icon.

<div id='dependencies'/>

## Dependencies :computer:

<div id='mvc'/>

## MVC Architecture :tokyo_tower:

I adopted an MVC (model view controller) architecture for this project, to organise my packages and program In an easy to understand for other to interpret. 

Take for example the [`controller`](src/main/java/com/sparta/kurtis/controller) package, which contains the [`CSVReader`](src/main/java/com/sparta/kurtis/controller/CSVReader.java) and [`EmployeeManager`](src/main/java/com/sparta/kurtis/controller/EmployeeManager.java) classes: both act as intermediaries between the [`model`](src/main/java/com/sparta/kurtis/model) and [`view`](src/main/java/com/sparta/kurtis/view) packages, processing the data and then outputting in a presentable manner.

I also created [`start`](src/main/java/com/sparta/kurtis/start) and [`util`](src/main/java/com/sparta/kurtis/util) packages for classes that did not fit within the other three packages (`start` for classes starting the program, `util` for utility classes used throughout the project).

<div id='threading'/>

## Threading :tshirt:

One of the requirements our trainer wanted was for the application to use multiple threads. I achieved this by [reading in a portion of the CSV file](src/main/java/com/sparta/kurtis/controller/CSVReader.java#L38-L43)), then [writing it to the database](src/main/java/com/sparta/kurtis/controller/CSVReader.java#L64-L68). Depending on how many threads the user specifies would determine the size of each portion of records was written in one go e.g. if writing the sample file containing 10k rows using 5 threads, then approximately 2000 would be written at a time (not accounting for duplicates).

I also incorporated [batching](src/main/java/com/sparta/kurtis/model/EmployeeDAO.java#L160-L170) into my program, whereby instead of only executing one query at a time, they could be added to a batch and ran all at once. Both batching and threading could be toggled by the user (inputting 0 threads and a boolean for batching), something I utilised when carrying out performance tests.

<div id='performance'/>

## Performance :performing_arts:

I conducted performance testing using parametrized tests using [junit params](pom.xml#L30-L35). I created a [CSV](src/test/java/com/sparta/kurtis/performance/CSVReaderPerformanceTests.java#L61-L83) of a variety of number of threads, as both batched and not batched. There is currently a bug in the testing that causes performances to be wildly inaccurate, this being related to threads and the nature in which the test calls upon the program.

#### Sample

#### Large

<div id='DAODTO'/>

## DAO & DTO :clipboard:

In order to interact with the database, I created a [Data Access Object](src/main/java/com/sparta/kurtis/model/EmployeeDAO.java), containing methods to [create a table](src/main/java/com/sparta/kurtis/model/EmployeeDAO.java#L102-L122) and [insert into it](src/main/java/com/sparta/kurtis/model/EmployeeDAO.java#L152-L181), as well as [selecting and printing it out](src/main/java/com/sparta/kurtis/model/EmployeeDAO.java#L58-L79). 

A [Data Transfer Object](src/main/java/com/sparta/kurtis/model/EmployeeDTO.java) was used to store the data from the CSV file in a compatible format for the database, something espeically important for the [Date](src/main/java/com/sparta/kurtis/model/EmployeeDTO.java#L75-L90) field.

<div id='future'/>

## Future Enhancements :customs:

- There is work to be done on fixing the performance tests to behave accurately and provide relevant data.
- The program lacks functionality testing as well, an instrumental aspect required to confirm the system works as it should and handles all sorts of situations in the correct way.
- Implementing ExecutorService for more intelligent thread management, thus improving performance would also be a feature I would look to add.
- Refactoring of [CSVReader](src/main/java/com/sparta/kurtis/controller/CSVReader.java) would also be on the cards, since I believe it has multiple responsibilities, thus not adhering to SOLID principles

---


[sparta_badge]: https://img.shields.io/badge/project-sparta-%23e43560?label=project&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACQAAAAkCAMAAADW3miqAAABZVBMVEVAIFHkNWBBH1FDHlHiNmBBH1LON2DlNWBcHVH+/v////3FNmFAHlRAG08/H1A+IFFhIU5BHlL9/fxBH05EHVL///xAHlH9//3//v3Y0dnjNl9LLFhCHVRDG08+IU79//v7//vMwtD4+Pi8scF6ZITjNmLfOF5BI1JDHU/28/ZuV3pGKFZEI1FnH08/HU339veDbI7kNV7iN11NHlE/HEv//////P/7/PrVOGPdN2JiIVE8IVFAHU8/H05UHk1AGU1HHUxNHEy8scCklamjk6iCa4p3YYDRNmbKNmLHNmHBNWHnNGCvMGBTH1NAHFHy7PLt6e/Vy9iwpLWwpLSoma6jkKaXf5t6ZoFvVnpgRG1hS2lYPWW1M11NMFqMLViLJ1iVLFd2KVd7JVR2IFOHKFJeIVFaH009GUjg2ePDuMfAucKzqLqxpbiQeZeMe5HaOGTQN2CqNGCmLF+dLFuEKllJJ1lGKFVpXQQhAAACNUlEQVR42qXQhXajWACAYZyQwmVxSrwtbNzdU7fU3X3c5fmHCHMgDcfmj4ePa9A4XBQ50GBxZzQqAMgW28BpWkYccVwwaDdCMNAreP5zVk+l+rg1CBREAu3HH1cbxKvuWQuJXPdhu2pgGAaT9raw2r1sTdUo3H3F8vkBckRqa3VujJDuXS2XN4xXKKdt9v0jIqI/q8NRdFjXHEjb2i4MEYLLL5ukhuVIUqutrc842rgRhmeA0umn2haskXr16inlcdYu0NCgKN3b1nUsp63dtqMcJ3P0IM7vH3ywAB+OJID2DGZmfO+x/mbw/1HJZBKxH7X8vG4avfoIgLD6YX7Y4uL8SsuOkDqBma0/s+nzt4p3FFVaTkO2xBSBGXltwyMzCxmFokIURalh5diO2EaKMIwcOeMBzMKceZ2vVCp8hT8u2geS6wQJY/oIlVV+cSU76BzYR/L3b75tEl/GKMSfvOl00szODm5HEN7tvaQebgt/UbFYZBgOmghncdBtQaPpIstLS0vLK78mFEv7ab/IscOFU6okSd5S7FNrAiFRVAAox6xmeIoKK4qi8uF3SWhq4HJWMg+yVPKGKG8kMd1ArfjRweHB4ftM2BUFg/5mOtpsFi/2XBBiJiAM0wkEOu4IRdHodXzQ2edddyQyq7uxQRmeHyB5OjrNhFRVCYVUhQ9HfkMuaC4kDfMq/JHggs72Z8ftf7wE0xcuJH1W18kdfPoRQLIcDZhFAyjOOc3kgIMQd/Fv6A8ksVwk6PNPnQAAAABJRU5ErkJggg==
