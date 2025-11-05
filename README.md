
Reflections on Using AI to code this project:
- I came up with the data model on paper first
- I did some research using Google in addition to the interactions I had with GitHub Copilot shown below.
- The list of websites I used are (not necessarily in order I accessed them):
- https://docs.github.com/en/authentication/connecting-to-github-with-ssh
- https://docs.github.com/en/copilot/how-tos/configure-personal-settings/configure-in-ide?tool=jetbrains
- https://www.jetbrains.com/help/idea/2025.2/supported-java-versions.html?Project_Category_and_Options&keymap=Windows
- https://medium.com/java-tips-and-tricks/how-to-calculate-holidays-in-java-d073977926fe
- https://blog.codimis.com/how-to-validate-and-normalize-java-record-classes-82cae6326df8
- https://www.baeldung.com/java-record-keyword
- https://www.baeldung.com/java-records-custom-constructor
- https://stackoverflow.com/questions/51820133/using-data-annotation-on-java-dto-class
- https://medium.com/@reetesh043/java-hashset-how-to-store-unique-elements-3ce9dbf630a8
- https://medium.com/@AlexanderObregon/formatting-numbers-for-display-with-decimalformat-in-java-0577c0fe0b52
- https://search.yahoo.com/search?fr=mcafee&type=E211US105G0&p=escape+%25+in+java+print+string
- https://zetcode.com/java/gson/
- https://stackoverflow.com/questions/63605794/is-there-any-way-of-using-records-with-inheritance
- https://www.baeldung.com/java-json-deserialize-record-gson
- https://howtodoinjava.com/java/date-time/add-subtract-business-days/
- https://howtodoinjava.com/gson/gson-typeadapter-for-inaccessibleobjectexception/
-I used Copilot to help configure my IDE (IntelliJ) to work with GitHub and Maven
-I also used CoPilot to generate tests and test cases, many of which I had to fix or change
- It made wrong assumptions about error handling
- It didn't find many of the edge cases, despite being given several
- Some of the autocompletes accessed the keys between Tool and ToolInfo incorrectly, making an error that was difficult to find because the types were correct and the code compiled
-It helped me generate JSON files from the array inputs in the design document, it was pretty good at this. I did check the results though
-Autocomplete was a little too aggressive and I found myself jumping around the file, randomlu overwritng stuff, especially if I rejected the suggested changes

Refinement Process:

