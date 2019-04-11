import spacy
import sys

nlp = spacy.load('es_core_news_sm')
doc = nlp(u'%s' % (sys.argv[1]))

for sent in doc.sents:
	for i, word in enumerate(sent):
		if word.head is word:
			head_idx = 0
		else:
			head_idx = word.head.i-sent[0].i+1
		print("%d\t%s\t%s\t%s\t%s\t%s\t%d\t%s\t%s\t%s"%(
			i+1,
			word,
			word.lemma_,
			word.pos_,
			'-',
			word.tag_,
			head_idx,
			word.dep_,
			'-', '-'))