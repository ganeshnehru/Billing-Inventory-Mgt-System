# Billing & Inventory Management System

## Description: 
This Billing & Inventory Management system maintains an internal, static database which contains an inventory of stocked items. In this system, a HashMap is used to store and update the quantity of items as an input file is processed successfully. The inventory holds items of 3 categories: Essential, Luxury, and Misc. The key of this HashMap would be a String value that contains the item name. The value of this HashMap would be an 'Item' object that contains the name, category, available quantity, and price for that item. Another HashMap is used to store, verify and maintain credit card numbers. For example, ff an input file contains a credit card number that is not already in the system, then that credit card number will be added to the HashMap. In order for an input file to be successfully processed, the following criteria must be met:

	1. Input quantity for items must be within the range of the available quantity of the inventory.

	2. Input file must contain a credit card number.

	3. Input items must satisfy the following categorical cap limits:
		- Quantity for ESSENTIAL items must have a cap limit of 3 items.
		- Quantity for LUXURY items must have a cap limit of 4 items.
		- Quantity for MISC items must have a cap limit of 6 items.

	4. Input item must be listed in the inventory.

If any of the following criteria is violated, the output would yield an 'output.txt' file, stating the items and reasons as to why the input file could not be processed. If all the criteria is met, then the inventory HashMap would be updated by reducing the quantity for the items associated with the input file; followed by an output of an 'invoice.csv' file that contains all the items in the input file, as well as the calculated total cost.

## Design Patterns Used:
1. Strategy: used for the purpose of reading an input file and an inventory file.

2. Chain of Commands: used for the purpose of checking if each item in input file satisfies the categorical cap limits.

3. Iterator: used for the purpose of iterating through a generated 'exceptions' ArrayList that contains the items that did not satisfy one or more criteria, listed above.

## Requirements:
- JDK 17 or greater

## To Run:
1. Open the 'BillingInventoryMgtSystem' project folder with IntelliJ.

2. Copy your input .csv file into the project directory. (Note: Default "input_file.csv" exists in directory)
	
	a. Optional: If you have a different inventory file, copy that file into the project directory as well.

3. Open 'InputReader.java' class, located in the 'FileReader' package.

	a. Within the 'InputReader.java' class, locate the 'readFile()' method.

	b. Relace the String parameter ("input_file.csv") in the Paths.get() method with the name of your input file.

### If you have a different inventory file other than the one provided, proceed to (4), otherwise skip to (5).

4.  Open 'InventoryReader.java' class, located in the 'FileReader' package.
	
	a. Within the 'InventoryReader.java' class, locate the 'readFile()' method.

	b. Relace the String parameter ("inventory_file.csv") in the Paths.get() method with the name of your inventory file.

5. Run 'Billing.java'.

	a. Based on the input file, an "invoice.csv" or "output.txt" file will be generated in the project directory.
