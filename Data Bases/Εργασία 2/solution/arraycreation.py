import csv
import ast

# Αρχικοποίηση λεξικών για την αποθήκευση μοναδικών λέξεων-κλειδιών και αντιστοιχίας με το keyword_id
keyword_dict = {}
movie_keyword_map = {}

# Διάβασμα του αρχείου keywords.csv
csvfile=open("keywords.csv", newline="", encoding="utf-8")
csvreader=csv.reader(csvfile)
"""
Κάθε γραμμή επιστρέφεται ως λίστα με τα πεδία της γραμμής ως στοιχεία της λίστας,
και χρησιμοποιούμε τα ευρετήρια της λίστας (όπως row[0], row[1])
για να αναφερθούμε στα διάφορα πεδία
"""
# Προσπερνάμε την πρώτη γραμμή, που περιέχει τους τίτλους των στηλών
next(csvreader) 
for row in csvreader:
    movie_id = int(row[0])
    keywords_json = ast.literal_eval(row[1])
        
    for keyword in keywords_json:
        keyword_id = keyword['id']
        keyword_name = keyword['name']
            
        # Αποθήκευση μοναδικών λέξεων-κλειδιών και αντιστοιχίας με το keyword_id
        if keyword_id not in keyword_dict:
            keyword_dict[keyword_id] = keyword_name
            
        # Δημιουργία αντιστοίχισης μεταξύ movie_id και keyword_id
        if movie_id not in movie_keyword_map:
            # Δημιουργούμε ένα νέο κλειδί στο λεξικό movie_keyword_map αν δεν υπάρχει ήδη και το αντιστοιχούμε με ένα νέο σύνολο (set)
            movie_keyword_map[movie_id] = set()
        movie_keyword_map[movie_id].add(keyword_id)
csvfile.close()

# Γράψιμο του αρχείου keyword.csv
csvfile=open("keyword.csv", "w", newline="", encoding="utf-8")
writer = csv.writer(csvfile)
writer.writerow(['id', 'name'])
for keyword_id, keyword_name in keyword_dict.items():
    writer.writerow([keyword_id, keyword_name])
csvfile.close()

# Γράψιμο του αρχείου haskeyword.csv
csvfile=open("haskeyword.csv", "w", newline="", encoding="utf-8")
writer = csv.writer(csvfile)
writer.writerow(['movie_id', 'keyword_id'])
for movie_id, keywords in movie_keyword_map.items():
    for keyword_id in keywords:
        writer.writerow([movie_id, keyword_id])
csvfile.close()
