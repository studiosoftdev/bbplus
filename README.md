# bbplus
Extended barebones language and interpreter from bbint project


Changes:

y Added [+ X Y Z] function. Performs X = Y + Z. Y and Z can be numbers or variables.

y Added [- X Y Z] function. Performs X = Y - Z. Y and Z can be numbers or variables.

y Added [* X Y Z] function. Performs X = Y * Z. Y and Z can be numbers or variables.

y Added [/ X Y Z] function. Performs X = Y / Z. Y and Z can be numbers or variables. Integer division only (currently).

y Added [set X Y] function. Performs X = Y. Initialises X if necessary. Y can be number or variable

y Modified [while X not 0 do] to [while X not Y do]. Y can be number or variable.

y Added While loop conditions: ==, >, <, in conjunction with not (!=).

y Added ability to insert empty lines

y Added [fn name]. Subroutines, no arguments.

y Added [endfn]. Denotes end of subroutine.

y Added [set]. Sets the variable to value or other variable.

y Added [out]. Prints the variables value on a new line.

y Added [outc]. Prints the character of the variables value, not new line.

y Added array structure... sorta. You can use the whole variable list as an array and index it with any variable. Can be referenced with any instruction

y Added [if X == Y]. Executes code iff X == Y where Y is a number or variable.

y Added [endif]. Denotes end of if statement.

y Added [goto]. Skips to line given by number, variable, or variable array reference.


(points with y are currently implemented, points with n arent currently implemented but planned to be)
