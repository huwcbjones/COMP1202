Test your Zoo against these files.

File					Expected Behaviour
zoo.txt					* Ignore invalid chars before zoo:
						* Bork at the file not starting with zoo:

enclosure.txt			* Bork that an animal has been added before an enclosure

syntax_zoo.txt			* Bork that a zoo: is followed by unknown chars, or
						* Ignore the invalid chars

syntax_enclosure_1.txt	* Throw up
syntax_enclosure_2.txt	* Throw up
syntax_enclosure_3.txt	* Add 50 waste to enclosure

syntax_food_1.txt		* Throw up
syntax_food_2.txt		* Throw up
syntax_food_3.txt		* Add 30 hay to zoo foodstore