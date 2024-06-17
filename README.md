# Knight Chessboard Assignment

This is an Android assignment project aiming to display my skills in Android development.

## Overview

The purpose of the app is to display an empty chessboard and give the ability to users to
designate a start and end position on this board. The application then calculates and displays
all potential routes a knight piece could take from the start to the end position in N moves
(default is 3). If there are no possible solutions, the application will notify the user.
Users have the option to reset the board and start over. The chessboard can be of any size from
6x6 to 16x16, and users can select their preferred size. Users also have the ability to set the
maximum number of moves. The path calculation process does not interfere with the main thread.
If the application is closed and then reopened, it recalls the last successful solution.

## Features

- The language used is Kotlin.
- The app renders an empty chessboard (default size is 8x8).
- The app contains two sliders that control the board size and their maximum moves.
- The board size can be from 6x6 to 16x16.
- The maximum moves can be from 1 to 5.
- The app contains a button that resets the board and the sliders.
- When the user selects a start and end position, the app calculates and displays all potential
  routes a knight piece could take from the start to the end position in N moves.
- The calculation process does not interfere with the main thread.
- If there are no possible solutions, the app will notify the user with a message.
- The rendering of the the paths is done using lines of different colors.
- The app remembers the last successful solution even if the app is closed and reopened using Room.
- The app utilizes the MVVM architecture pattern and clean architecture.
- The app has unit tests for the view-models.
- The structure of the app is easy to understand and follow by trying to utilise the SOLID 
  principles as much as possible.
- The app uses Coroutines to handle asynchronous tasks.
- The app uses LiveData to observe changes in the data.
- The UI is handled using Jetpack Compose.
- The app supports dark and light theme.
- It also saves the theme selected by the user as well.
- Utilises a loader while the app is calculating the paths.
- Readability: The code is written in a way that is easy to understand and follow.
- Reusability: The code is written in a way that is easy to reuse.
- Innovative: Tried to use the latest libraries and technologies when possible.
- Custom App icon.

## Incomplete Features

- Due to lack of time, I was not able to implement a truly complete unit test suite. I only managed
  to write unit tests for the view-model.
- Also due to lack of time, I could not apply a truly complete Separation of Concerns. I tried to
  separate the concerns as much as possible, but there is still room for improvement.
- There would also be some cases where it would be more beneficial to use LiveData instead of
  StateFlows.

## Architecture

The architecture pattern I used in the project is MVVM. I chose this pattern
because it allows for better separation of concerns and easier testing.

## Libraries Used

- [Room](https://developer.android.com/jetpack/androidx/releases/room): Used for storing data in a local database.
- [Gson](https://github.com/google/gson): Used for parsing JSON.
- [Mockito](https://site.mockito.org/): Used for unit testing.

## Getting Started

1. Clone the repository: `git clone https://github.com/George-Cain/ChessboardAssignment.git` or download the zip.
2. Open the project in Android Studio.
3. Make sure the emulator or the mobile device is API 30 or higher (preferably API 34+).
4. Run the app.

## How to Use

1. Launch the app.
2. Choose the board size and the maximum moves or leave them as default.
3. Select a start and end position on the board.
4. The app will then calculate and display all potential routes a knight piece could take from the
   start to the end position in however many moves you selected.
5. Reset the board or select a new start and end position to begin again. 
6. Optionally, you can change the theme of the app. 
7. Also optionally, close the app and reopen it. The app will remember the last successful solution 
   as well as the state of the slides and the theme you selected. 
8. Enjoy!

## License

MIT License

Copyright (c) [2024] [George Apergis]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

## Contact

- Name: George Apergis
- Email: igeorge.ap@gmail.com
- LinkedIn: https://www.linkedin.com/in/GApergis/
- Phone: +30 6945336738
