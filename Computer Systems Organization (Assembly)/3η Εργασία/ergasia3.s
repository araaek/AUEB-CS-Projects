# 
# 
# 

.text
    .globl _start

_start:
# main
main:
    # Initialize variables
    li $t1, 0         # mikosA
    sw $t1 , mikosA
    li $t1, 0         # mikosB
    sw $t1 , mikosB
    li $t1, 0         # mikosC
    sw $t1 , mikosC
    
    menu:
        jal readOption
        bgtz $v0, processOption
        j exit

readOption:
    li $v0, 4
    la $a0, prompt9
    syscall
    li $v0, 4
    la $a0, prompt10
    syscall
    li $v0, 4
    la $a0, prompt11
    syscall
    li $v0, 4
    la $a0, prompt12
    syscall
    li $v0, 4
    la $a0, prompt13
    syscall
    li $v0, 4
    la $a0, prompt14
    syscall
    li $v0, 4
    la $a0, prompt15
    syscall
    li $v0, 4
    la $a0, prompt16
    syscall
    li $v0, 4
    la $a0, prompt17
    syscall
    li $v0, 4
    la $a0, prompt18
    syscall
    li $v0, 4
    la $a0, prompt9
    syscall
    li $v0, 4
    la $a0, prompt19
    syscall
    li $v0, 5
    syscall
    jr $ra

processOption:
    # Switch statement to handle menu options
    beq $v0, 1, readArrayA
    beq $v0, 2, readArrayB
    beq $v0, 3, createSparseA
    beq $v0, 4, createSparseB
    beq $v0, 5, createSparseC
    beq $v0, 6, displaySparseA
    beq $v0, 7, displaySparseB
    beq $v0, 8, displaySparseC
    beq $v0, 0, exit

    readArrayA:
        li $v0, 4
        la $a0, prompt1
        syscall
        la $t0, pinA
        jal readPin
        j menu

    readArrayB:
        li $v0, 4
        la $a0, prompt2
        syscall
        la $t0, pinB
        jal readPin
        j menu

readPin:
    # Read values into the array
    li $t1, 0      # i=0
    li $t2, 10
    loop_readPin:
        beq $t1, $t2, end_readPin  # if i>=10, exit loop
        
        # Printing "Position "
        li $v0, 4                  # system call code for print string
        la $a0, prompt20 
        syscall 
        
        # Printing the index
        li $v0, 1                  # system call code for print int
        move $a0, $t1      
        syscall
        
        # Printing ":"
        li $v0, 4                  # system call code for print string
        la $a0, prompt21 
        syscall 
        
        # Reading integer from console
        li $v0, 5          # system call code for read int
        syscall            # get int from user, returned in $v0
        sw $v0, ($t0)      # put integer in the array
        
        addi $t1, $t1, 1
        addi $t0, $t0, 4
                                
        j loop_readPin
    end_readPin:
        jr $ra

createSparseA:
    li $v0, 4
    la $a0, prompt3
    syscall
    la $a0, pinA
    la $a1, SparseA
    jal createSparse
    sw $v0, mikosA
    move $t1, $v0
    srl $t1, $t1, 1    # divide by 2
    li $v0, 1
    move $a0, $t1
    syscall
    li $v0, 4
    la $a0, prompt22
    syscall
    j menu

createSparseB:
    li $v0, 4
    la $a0, prompt4
    syscall
    la $a0, pinB
    la $a1, SparseB
    jal createSparse
    sw $v0, mikosB
    move $t2, $v0
    srl $t2, $t2, 1    # divide by 2
    li $v0, 1
    move $a0, $t2
    syscall
    li $v0, 4
    la $a0, prompt22
    syscall
    j menu

createSparseC:
    li $v0, 4
    la $a0, prompt5
    syscall
    la $a0, SparseA
    lw $a1 mikosA
    #move $a1, $t1
    la $a2, SparseB
    lw $a3 mikosB
    move $a3, $t2
    la $t7, SparseC
    jal adding
    # sw $v0 mikosC
    # move $t3, $v0
	lw $t3, mikosC
    srl $t3, $t3, 1    # divide by 2
    li $v0, 1
    move $a0, $t3
    syscall
    li $v0, 4
    la $a0, prompt22
    syscall
    j menu

createSparse:
    # Create a sparse array from the input array
    li $t0, 0      # i
    li $t1, 0      # k
    li $v0, 0      # Initialize the count of non-zero values
    loop_createSparse:
        beq $t0, 10, end_createSparse
        lw $t2, 0($a0)                       # Load the current element
        beqz $t2, zero_element_createSparse  # Skip if the element is zero
        sw $t0, 0($a1)                       # Store the index of the non-zero element
        addi $a1, $a1, 4                     # Move to the next index position
        sw $t2, 0($a1)                       # Store the non-zero element
        addi $a1, $a1, 4                     # Move to the next element in the sparse array
        addi $t1, $t1, 2                     # Increment the count of non-zero values
        zero_element_createSparse:
            addi $a0, $a0, 4                 # Move to the next element in the input array
            addi $t0, $t0, 1
        j loop_createSparse
    end_createSparse:
        move $v0, $t1
        jr $ra


displaySparseA:
    li $v0, 4
    la $a0, prompt6
    syscall
    la $a0, SparseA
    lw $a1 mikosA
    jal printSparse
    j menu

displaySparseB:
    li $v0, 4
    la $a0, prompt7
    syscall
    la $a0, SparseB
    lw $a1 mikosB
    jal printSparse
    j menu

displaySparseC:
    li $v0, 4
    la $a0, prompt8
    syscall
    la $a0, SparseC
    lw $a1, mikosC
    jal printSparse
    j menu

printSparse:
    # Print the sparse array
    li $t0, 0      # i
    
    move $t8, $a0
    loop_printSparse:
        beq $t0, $a1, end_printSparse
        lw $t1, 0($t8)      # Load the index
        
        # Printing "Position"
        li $v0, 4
        la $a0, prompt20
        syscall
        
        # Printing ": "
        li $v0, 4          # system call code for print string
        la $a0, prompt21 
        syscall 
        
        li $v0, 1
        move $a0, $t1
        syscall
        
        li $v0, 4
        la $a0, space
        syscall
        
        # Printing "Value: "
        li $v0, 4
        la $a0, prompt23
        syscall
            
        lw $t2, 4($t8)     # Load the value ***
        li $v0, 1
        move $a0, $t2
        syscall
        
        li $v0, 4
        la $a0, newline
        syscall
        addi $t8, $t8, 8  # Move to the next element in the sparse array
        addi $t0, $t0, 2
        j loop_printSparse
    end_printSparse:
    jr $ra

adding:
    # Add two sparse arrays and store the result in another sparse array
    # Arguments:
    #   $a0 - address of SparseA
    #   $a1 - mikosA
    #   $a2 - address of SparseB
    #   $a3 - mikosB
    #   $t7 - address of SparseC
    # Return:
    #   The length of SparseC in $t3
	
    li $t0, 0      # a
    li $t1, 0      # b
    li $t2, 0      # c
    li $t3, 0      # Initialize the count of non-zero values
        
    la $a0, SparseA
    lw $a1, mikosA
    la $a2, SparseB
    lw $a3 mikosB
    la $t7, SparseC
        
    loop_add:
        beq $t0, $a1, add_remainingB
        beq $t1, $a3, add_remainingA
		        
        lw $t4, 0($a0)  # Load index from SparseA
        lw $t5, 0($a2)  # Load index from SparseB
        blt $t4, $t5, add_a
        bgt $t4, $t5, add_b
        
        add_values:
        lw $t6, 4($a0)  # Load value from SparseA
        lw $t8, 4($a2)  # Load value from SparseB
        
        add $t6, $t6, $t8  # Add the values
        sw $t4, 0($t7)     # Store the index in SparseC
        addi $t7, $t7, 4   # Move to the next index position
        addi $a0, $a0, 4   # Move to the next element in SparseA
        addi $a2, $a2, 4   # Move to the next element in SparseB
        
        sw $t6, 0($t7)     # Store the result in SparseC
        addi $t7, $t7, 4   # Move to the next element in SparseC
        addi $a0, $a0, 4   # Move to the next element in SparseA
        addi $a2, $a2, 4   # Move to the next element in SparseB
        addi $t0, $t0, 2   # itan 1
        addi $t1, $t1, 2   # itan 1
        addi $t3, $t3, 2   # Increment the count of non-zero values
        j loop_add
        
        add_a:
        sw $t4, 0($t7)     # Store the index in SparseC
        addi $t7, $t7, 4   # Move to the next element in SparseC    
        addi $a0, $a0, 4   # Move to the next element in SparseA
        lw $t4, 0($a0)
        sw $t4, 0($t7)     # Store the value in SparseC
        addi $t7, $t7, 4   # Move to the next element in SparseC
        addi $a0, $a0, 4   # Move to the next element in SparseA
        addi $t3, $t3, 2   # Increment the count of non-zero values
        addi $t0, $t0, 2   #itan 1
        j loop_add
        
        add_b:
        sw $t5, 0($t7)     # Store the index in SparseC
        addi $t7, $t7, 4   # Move to the next element in SparseC
        addi $a2, $a2, 4   # Move to the next element in SparseB
        lw $t5, 0($a2)
        sw $t5, 0($t7)     # Store the value in SparseC
        addi $t7, $t7, 4   # Move to the next element in SparseC
        addi $a2, $a2, 4   # Move to the next element in SparseB
        addi $t3, $t3, 2   # Increment the count of non-zero values
        addi $t1, $t1, 2   # itan 1
        j loop_add
        
        
        add_remainingA:
       	bgt $t0, $a1, end_add
		lw $t4, 0($a0)     # Load index from SparseA
        addi $a0, $a0, 4
        lw $t6, 0($a0)     # Load value from SparseA
        sw $t4, 0($t7)     # Store the index in SparseC
        addi $t7, $t7, 4   # Move to the next index position
        sw $t6, 0($t7)     # Store the value in SparseC
        addi $t7, $t7, 4   # Move to the next element in SparseC
        addi $t3, $t3, 2   # Increment the count of non-zero values
        addi $a0, $a0, 4   # Move to the next element in SparseA
        addi $t0, $t0, 2   #itan 1
        j add_remainingA
        
        
        add_remainingB:
		bgt $t1, $a3, end_add
        lw $t4, 0($a2)     # Load index from SparseB
        addi $a2, $a2, 4
        lw $t6, 0($a2)     # Load value from SparseB
        sw $t4, 0($t7)     # Store the index in SparseC
        addi $t7, $t7, 4   # Move to the next index position
        sw $t6, 0($t7)     # Store the value in SparseC
        addi $t7, $t7, 4   # Move to the next element in SparseC
        addi $t3, $t3, 2   # Increment the count of non-zero values
        addi $a2, $a2, 4   # Move to the next element in SparseB
        addi $t1, $t1, 2   # itan 1
        j add_remainingB
            
    end_add:
    addi $t3, $t3, -2
	
    sw $t3, mikosC
    jr $ra
	
exit:
    # Exit the program
    li $v0, 10
    syscall

.data
    prompt1:  .asciiz "Reading Array A\n"
    prompt2:  .asciiz "Reading Array B\n"
    prompt3:  .asciiz "Creating Sparse Array A\n"
    prompt4:  .asciiz "Creating Sparse Array B\n"
    prompt5:  .asciiz "Creating Sparse Array C = A + B\n"
    prompt6:  .asciiz "Displaying Sparse Array A\n"
    prompt7:  .asciiz "Displaying Sparse Array B\n"
    prompt8:  .asciiz "Displaying Sparse Array C\n"
    prompt9:  .asciiz "\n-----------------------------\n"
    prompt10: .asciiz "1. Read Array A\n"
    prompt11: .asciiz "2. Read Array B\n"
    prompt12: .asciiz "3. Create Sparse Array A\n"
    prompt13: .asciiz "4. Create Sparse Array B\n"
    prompt14: .asciiz "5. Create Sparse Array C = A + B\n"
    prompt15: .asciiz "6. Display Sparse Array A\n"
    prompt16: .asciiz "7. Display Sparse Array B\n"
    prompt17: .asciiz "8. Display Sparse Array C\n"
    prompt18: .asciiz "0. Exit"
    prompt19: .asciiz "Choice? "
    prompt20: .asciiz "Position "
    prompt21: .asciiz ": "
    prompt22: .asciiz " values\n"
    prompt23: .asciiz " Value: "
    space: .asciiz " "
    newline: .asciiz "\n"
    .align 2
    pinA: .space 40     # 10 integers * 4 bytes each
    .align 2
    pinB: .space 40     # 10 integers * 4 bytes each
    .align 2
    SparseA: .space 80  # 20 integers * 4 bytes each
    .align 2 
    SparseB: .space 80  # 20 integers * 4 bytes each
    .align 2 
    SparseC: .space 80  # 20 integers * 4 bytes each
    mikosA: .space 4    # integer
    mikosB: .space 4    # integer
    mikosC: .space 4    # integer
    op: .space 4        # integer
    
