# bbplus
Extended barebones language and interpreter from bbint project


Changes:

- Added [+ X Y Z] function. Performs X = Y + Z. Y and Z can be numbers or variables.
- Added [- X Y Z] function. Performs X = Y - Z. Y and Z can be numbers or variables.
- Added [* X Y Z] function. Performs X = Y * Z. Y and Z can be numbers or variables.
- Added [/ X Y Z] function. Performs X = Y / Z. Y and Z can be numbers or variables. Integer division only.
- Added [% X Y Z] function. Performs X = Y % Z. Y and Z can be numbers or variables.
- Added [set X Y] function. Performs X = Y. Initialises X if necessary. Y can be number or variable
- Modified [while X not 0 do] to [while X not Y do]. Y can be number or variable.
- Added While loop conditions: ==, >, <, in conjunction with not (!=).
- Added ability to insert empty lines
- Added [fn name]. Subroutines, no arguments.
- Added [endfn]. Denotes end of subroutine.
- Added [set]. Sets the variable to value or other variable.
- Added [out]. Prints the variables value, not on a new line.
- Added [outc]. Prints the character of the variables value, not new line.
- Added array structure... sorta. You can use the whole variable list as an array and index it with any variable. Can be referenced with any instruction.
- Added [data]. This will take the operand and store each character as integers in the array list sequentially with their own naming structure.
- Added [if X == Y]. Executes code iff X == Y where Y is a number or variable.
- Added [endif]. Denotes end of if statement.
- Added [goto]. Skips to line given by number, variable, or variable array reference.
- Added [skip]. Skips to next line. Can be useful in some contexts, notably used with a goto to skip to the end of file.
- Retains full compatibility with original language.


Program commentary:
Wow this one took a lot longer than I thought it would. Just finishing this now on Monday 13.27. A lot of that was because I spent wayyy too long trying to fit an else structure into this, and that just got really hairy and I ran out of time to deal with it. I realised that you can make an equivalent if statement so there's no harm in foregoing it.
A lot of these additions were based on the wiki, but a fair number of these came from my own wants and needs in making test programs, and, later, the lab1 programs.
It's no longer dinky, moving from 69 lines of sparsely commented jank to 271 lines of sparsely commented jank, but I think the return of a decently featured programming language is worthwhile.

The original language reminded me a bit of assembly and so I wanted to preserve some of that spirit with my additions - that's why there's no simple print instruction: you can make a print subroutine yourself as I have done, just using the simple outc which outputs one character. It also inspired me to implement the only array structure in the whole thing: the VARS[] array. This is directly accessing the variable list, so I think it's quite powerful. The data instructions load in straight to that list so if you're keeping track of where your variables are (added in order of appearance) then you can actually do a decent amount with it. Well, I just used it for printing but I imagine you can do more. You can probably program your own array variables if you're happy with that taking up quite a few lines, just need a couple start and end indexing variables.

I'm pretty happy with how this turned out, it runs the various examples I've made including all of lab1 remade in bbplus (would truly be bb++ if I had another week to implement OOP :p) very well. If I had more time, I'd add in support for else statements; floating point values and possibly other data types, actual array variables; more system variables like the program counter (though, you do have some control of this with the goto instruction) and various stack pointers (I have 5 stacks and I don't know how I could use less lol); and maybe even stuff like actual functions.

Thanks for checking this out :)
