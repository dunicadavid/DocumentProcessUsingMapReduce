# DocumentProcessUsingMapReduce
[EN]

This is an Java algorithm that evaluates the rang, maximum lenght of a word and it's number of appearances of a file. The rang of a word is the lenght + 1 number of fibonacci's string multiply by the number of appearces of that lenght devided by the total number of words. The rang of the file is the sum of all word rangs.
This algorithm uses a map reduce model. Map threads finds the words' lenght and number of appearances and add the data into a map. Reduce threads finds the rang and max word for each file.

[RO]

Intelegerea implementarii:
	
	~Tema2.java: in aceasta clasa in functia main am prelucrat datele de la tastatura cat si cele din fisier, apoi am creat primul set de workeri(MyThreadMap), dupa finalul acestora am prelucrat datele obtinute. In continuare am creat urmatorul calup de workeri (MyThreadReduce), ca mai apoi sa ii sortez si dupa sa scriu in fisier;


    public static List<TaskMap> tasks = new ArrayList<TaskMap>();              		//Lista de dictionare a taskurilor (contine atat ceea ce am nevoie cat si ceea ce trb sa aflu)
    public static List<TaskReduce> tasksReduce = new ArrayList<TaskReduce>();		//Lista de dictionare reduced a taskurilor dupa primul set de workeri (contine atat ceea ce am nevoie cat si ceea ce trb sa aflu)
    public static int workers;								//nr de workeri

    public static List<String> fileList = new ArrayList<String>();			//Lista de Stringuri ce contine datele din fisiere
    public static List<Integer> fileListSizes = new ArrayList<Integer>();		//Lista cu lungimea fiecarui fisier
    public static int size;								//sizeul citit primul fisier referitor la lungimea impartirii in stringuri
    public static int filesNumber;							//nr de fisiere care urmeaza sa fie citite

	liniile[ 24-26 ] citesc de la tastatura
	liniile[ 29-33 ] citesc din primul fisier specificat de la tastatura
	liniile[ 35-64 ] pentru fiecare fisier specificat in fisierul amintit mai sus imi completez lista de Taskuri in dictionarul TaskMap si imi creez lista de stringuri cu continutul fisierelor (fileList) => la finalul structurii
				repetitive am completata lista de taskuri.
	liniile[ 71-84 ] creez workerii (primul set de threaduri) MyThreadMap
	liniile[ 87-101] parcurg lista de taskuri "tasks" si introduc datele calculate mai sus in lista "tasksReduce" conform cerintei
	liniile[104-117] creez workerii (al doilea set de threaduri) MyThreadReduce
	liniile[121-134] sortez si apoi afisez in fisier


	~TaskMap.java: clasa ce retine datele unui task si ceea ce se calculeaza in el pentru etape de map

    public String file;					//numele fisierului din care se face cautarea
    public int fileIndex;				//indexul din lista de Stringuri "fileList" in care am introdus continutul fisierelor 
    public long size;					//lungimea stringului
    public int offset;					//offsetul (characterul de la care incepe taskul)
    public int maxLenght;				//lungimea maxima a unui cuvant 
    public List<String> maxWord;			//lista de cuvinte maxim din secventa
    HashMap<Integer, Integer> cache;			//mapa de lungime:nrAparitii

	clasa mai contine :
		-> constructor (liniile 15-23)
		-> CheckEndOfWord : functie de verificare daca un anumit caracter reprezinta spatiu sau face parte dintr-un cuvant 
		-> checkAndAddInMap : functie care verifica daca un key este in map ->daca da incrementeaza valoarea acelui key ->daca nu atunci adauga aceasta noua cheie 


	~MyThreadMap.java: clasa cu implements Runnable aceasta rezolva cerinta primilor workeri pe threaduri. Cum am identificat corect cuvintele dintr-un interval task? pornesc de la primul character al intervalului meu (adica offset + position, unde initial position = 0). verifica daca acest caracter face parte dintr-un cuv sau nu (cu functia CHECKENDOFWORD)daca nu face atunci parcurg stringul pana gasesc un incaput de cuv; daca face parte dintr-un cuv atunci trebuie sa verific si caracterul precedent: daca NU face parte dintr-un cuv atunci inseamna ca inceputul taskului este si inceputul de cuvant, altfel inseamna ca characterul pe care ma aflu la pozitia offset+position este deja luat in calcul in alt thread deci merg intr- un loop pana cand gasesc spatiu. Daca am gasit spatiu (incrementand position) inseamna ca acum trb sa parcurg stringul pana gasesc un alt inceput de cuvant.  DUPA ACEASTA SECVENTA TEORETIC AM AJUNS LA INCEPUTUL UNUI CUVANT NOU. pentru a contoriza de acum incolo cuvintele trb sa ma mai asigur ca incrementand position sa nu fi depasit sizeul. Daca am depasit sizeul atunci nu ma mai intereseaza ies din acest task nu am ce calcula in el, dar daca nu este depasit, pot incepe sa caut cuvinte. 
	Acum merg caracter cu caracter cat timp position < size dupa urmatoarea regula: daca characterul face parte din cuvant incrementez variablia lenght si adaug in stringul word caracterele gasite. Daca am dat de final de cuvant atunci modific in elementul respectiv din "tasks" maxLenght daca este cazul, adaug cuvantul gasit si adaug in Mapa noua combinatie(sau o incrementez daca exista; folosesc functia checkAndAddInMap).
	In momentul asta mai ramane un singur lucru de facut sa verific daca la finalul secventei repetitive de cautare cuvantul chiar s-a terminat, pot verifica asta daca lenght > 0, in cazul in care lenght > 0 atunci continui sa incrementez position pana cand gasesc un spatiu, astfel iau tot cuvantul inceput in taskul meu. 

	in cod : liniile 19-41 fac verificare initiala sa ma duce cu position unde incepe un cuvand valid
		 liniile 43-63 imi cauta cuvintele din intervalul ramas [offset + position, offset + size] 
		 liniile 66-87 daca lenght > 0 continui sa contorizez cuvantul la care ma aflu pana dau de spatiu sau se termina stringul(chiar daca ies din intervalul meu de cautare) + trb mentionat ca ultimul task nu poate intra in
			aceasta secventa
		 liniile 89-99 se verifica din nou daca lenght > 0 (este necesar deoarece scrierea in tasks se face cand se da de 'spatiu', astfel fara aceasta verificare ultimul cuvant era uitat). 
  

	~TaskReduce.java: clasa ce retine datele unui task si ceea ce se calculeaza in el pentru etape de reduce

    List<HashMap<Integer, Integer>> cacheList;		//lista de mape ale taskurilor cu acelasi file
    public List<String> maxWord;			//lista cuvintelor maxime din fiecare task cu acelasi file
    public String file;					//fileul pe care il reprezinta

    public int maxLenght;				//lungimea maxima din fisier a unui cuv --trb calc
    public int nrOfMax;					//nr de cuv cu dim maxima --trb calc
    public double rang;					//rangul --trb calc

	clasa mai contine :
		-> constructor (liniile 15-19)
		-> getFibonacci : calculeaza nr respectiv din sirul lui fibonacci (conform cerintei)

	~MyThreadMap.java: clasa cu implements Runnable aceasta rezolva cerinta celor de-ai doilea workeri pe threaduri. 

		liniile 17-22 cauta in lista de cuvinte cuvantul cu lungime maxima si ii retine marimea => astfel aflu maxLenght
		liniile 25-32 caut cate cuvinte de lungime maxima parcurgand lista de mape si cautand in fiecare mapa keyu maxLenghy => astfel aflu nrOfMax
		liniile 35-39 calculez numaratorul rangului adunand fiecare inmultire dintre getFibonacci (key) cu value in 'aux'
		linia 41 calculeaz rang   (aux/nrWords)


	~SortByRang.java clasa auxiliara pentru sort in main linia 121.




