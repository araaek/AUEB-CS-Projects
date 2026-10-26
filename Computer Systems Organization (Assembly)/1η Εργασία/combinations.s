# 

.text
    .globl _start
_start:
    # System.out.print ("Enter number of objects in the set (n <= 12): ");
    li $v0, 4          # Print string
    la $a0, n_prompt
    syscall

    # int n = in.nextInt();
    li $v0, 5          # Read integer
    syscall
	sw $v0, n          # Save the integer in variable n
    move $s0, $v0      # Store n in $s0

    # System.out.print ("Enter number to be chosen (k <= 12): ");
	li $v0, 4          # Print string
    la $a0, k_prompt
    syscall

    # int k = in.nextInt();
    li $v0, 5          # Read integer
    syscall
	sw $v0, k          # Save the integer in variable k
    move $s1, $v0      # Store k in $s1

   
    # if (n > 12 or k > 12) then print invalid2 message and exit
	li $t8, 12
	bgt $s0, $t8, invalid2
	bgt $s1, $t8, invalid2


    # if (n<k or k<0) then print invalid1 message and exit
	blt $s0,$s1, invalid1
	bltz $s1, invalid1
	


    # Calculate Factorial_n
    li $t0, 1          # Initialize Factorial_n to 1
    li $t2, 1          # Initialize i to 1
    loop_factorial_n:
        bgt $t2, $s0, done_factorial_n
        mul $t0, $t0, $t2
        addi $t2, $t2, 1
        j loop_factorial_n
    done_factorial_n:

    # Calculate Factorial_k
    li $t1, 1          # Initialize Factorial_k to 1
    li $t3, 1          # Initialize i to 1
    loop_factorial_k:
        bgt $t3, $s1, done_factorial_k
        mul $t1, $t1, $t3
        addi $t3, $t3, 1
        j loop_factorial_k
    done_factorial_k:

    # Calculate Factorial_n_k
    li $t4, 1          # Initialize Factorial_n_k to 1
    li $t5, 1          # Initialize i to 1
    sub $t6, $s0, $s1  # n - k
    loop_factorial_n_k:
        bgt $t5, $t6, done_factorial_n_k
        mul $t4, $t4, $t5
        addi $t5, $t5, 1
        j loop_factorial_n_k
    done_factorial_n_k:

    # Calculate the combination 
    div $t0, $t1       # Factorial_n / Factorial_k
    mflo $t0           # Store the result in $t0
    div $t0, $t4       # (Factorial_n / Factorial_k) / Factorial_n_k
    mflo $t0           # Store the result in $t0

    # Print the result
	li $v0, 4          # Print string "C("
    la $a0, result_prompt1
    syscall
	
	li $v0, 1          # Print integer n
	move $a0, $s0      # n
	syscall
	
	li $v0, 4          # Print string ", "
    la $a0, result_prompt2
    syscall
	
	li $v0, 1          # Print integer k
	move $a0, $s1      # k
	syscall
	
	li $v0, 4          # Print string ") = "
    la $a0, result_prompt3
    syscall
	
    li $v0, 1          # Print integer result
	move $a0, $t0      # result
    syscall
	
	li $v0, 4          # Print new line
    la $a0, new_line
    syscall
	j end

invalid1:
    li $v0, 4          # Print string
    la $a0, invalid_input1
    syscall
    j end

invalid2:
    li $v0, 4          # Print string
    la $a0, invalid_input2
    syscall
    j end

end:
    # Exit program
    li $v0, 10
    syscall


.data
    n: .space 4
	k: .space 4
	n_prompt: .asciiz "Enter number of objects in the set (n <= 12): "
    k_prompt: .asciiz "Enter number to be chosen (k <= 12): "
    invalid_input1: .asciiz "Please enter n >= k >= 0\n"
	invalid_input2: .asciiz "Please enter n <= 12 and k <= 12\n"
    result_prompt1: .asciiz "C("
	result_prompt2: .asciiz ", "
	result_prompt3: .asciiz ") = "
	new_line: .asciiz "\n"
	