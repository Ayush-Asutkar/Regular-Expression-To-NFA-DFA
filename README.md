# Compiler Design Lab

---

## Conversion of Regular Expression to NFA 
## Following are my details for assignment submission:
<p>Name: &nbsp;&nbsp;&nbsp;&nbsp;Ayush Vinayak Asutkar</p>
<p>Roll No.: &nbsp;20CS01057</p>
<p>Semester: &nbsp;7th</p>
<p>Year of study: &nbsp;4th year</p>
<p>Subject: &nbsp;&nbsp;Compiler Design Laboratory</p>
<p>Assignment: &nbsp;Assignment - 1</p>

---


### Problem Statement
<p>Write a program to generate NFA from a given Regular Expression. Consider some alphabet of your choice.
Given any Regular Expression over the alphabet as input, your program should generate the corresponding NFA.</p>

<p>(Optional: Implement methods to transform to DFA, DFA minimization, simulation, etc.)</p>

### How to run
1. Clone the repository: 
2. Open in your favourite editor. (The editor used while making this project was Intellij IDEA)
3. If you want to run the complete project, run the Main.java in src folder.
4. If you want to check/run any specific conversions, go to src, and then choose the conversion you wish to run. Inside that class a small main method is written, make changes as per request, and run the main. (An example is already written at that place)

### Allowed alphabet
<p>The allowed alphabet are lowercase and uppercase latin letters.</p>
<p>NOTE: The uppercase and lowercase latin letters will be treated separately</p>

### Regular Expression to Epsilon NFA
<p>The given regular expression is first converted into its postfix expression.
And this postfix expression is then evaluated. The operators are applied using [Thompson's Construction](https://en.wikipedia.org/wiki/Thompson%27s_construction) </p>
<p>The implementation can be found at "conversionhelper" package -> "PostfixConversionHelper" and "PostFixREToEpsilonNFAConversionHelper"</p>

### Epsilon NFA to NFA
<p>Referred to [link](https://www.tutorialspoint.com/how-to-convert-nfa-with-epsilon-to-without-epsilon)</p>
<p>The implementation can be found at "conversionhelper" package -> "EpsilonNFAToNFAConversionHelper"</p>

### NFA to DFA
<p>This part was optional for the assignment. Will try to update the code on github</p>