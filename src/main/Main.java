package main;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
//import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations.CorefChainAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;

/**
 * Class to practice using the wordnet api
 * 
 * @author dustin
 * 
 */
public class Main {

	// to-do this should be replaced by wordnet's representation for each of
	// these
	enum WORDTYPE {
		NOUN, VERB, ADJECTIVE, ADVERB, ARTICLE
	};

	public static void main(String[] args) {

		
		System.out.println("Start of program\n");
		
		// MaxentTagger tagger = new
		// MaxentTagger("taggers/english-bidirectional-distsim.tagger");
		// MaxentTagger tagger = new MaxentTagger(
		// "taggers/english-left3words-distsim.tagger");

		// String tagged = tagger.tagString(exampleSentence);

		// Set up wordnet
		// construct the URL to the Wordnet dictionary directory
		{/*
		String wnhome = System.getenv("WNHOME");
		String path = wnhome + File.separator + "dict";
		System.out.println("Trying path: " + path);
		URL url = null;
		try {
			url = new URL("file", null, path);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// construct the dictionary object and open it
		IDictionary dict = new Dictionary(url);
		try {
			dict.open();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// creates a StanfordCoreNLP object, with POS tagging, lemmatization,
		// NER, parsing, and coreference resolution
		Properties props = new Properties();
		props.setProperty("annotators",
				"tokenize, ssplit, pos, lemma, ner, parse, dcoref");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

		// read some text in the text variable
		//String exampleSentence = "It's warm outside and I'm selling good tickets for a stupid concert tonight.";
		//String exampleSentence = "Kostas is a wonderful friend and he makes me happy and inspired";
		//String exampleSentence = "Two things are infinite: the universe and human stupidity; and I'm not sure about the universe.";
		//String exampleSentence = "Dustin likes to eat a lot of old bananas";
		//String exampleSentence = "Dustin is upset when hookers turn him down in a mean way";
		String exampleSentence = "The fat cow ate the fluffy grass and defecated smelly poop";

		// create an empty Annotation just with the given text
		Annotation document = new Annotation(exampleSentence);

		// run all Annotators on this text
		pipeline.annotate(document);

		// these are all the sentences in this document
		// a CoreMap is essentially a Map that uses class objects as keys and
		// has values with custom types
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);

		System.out.println("[Starting Sentence] " + exampleSentence);
		for (String sim : similarSentences(sentences.get(0), dict)) {
			System.out.println("[New Sentence]     " + sim);
		}

		for (CoreMap sentence : sentences) {
			// traversing the words in the current sentence
			// a CoreLabel is a CoreMap with additional token-specific methods
			for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
				// this is the text of the token
				String word = token.get(TextAnnotation.class);
				// this is the POS tag of the token
				String pos = token.get(PartOfSpeechAnnotation.class);
				// this is the NER label of the token
				String ne = token.get(NamedEntityTagAnnotation.class);

//				System.out.println("word = " + word + ", pos = " + pos
//						+ ", ne = " + ne);
			}

			// this is the parse tree of the current sentence
			Tree tree = sentence.get(TreeAnnotation.class);

			// this is the Stanford dependency graph of the current sentence
			SemanticGraph dependencies = sentence
					.get(CollapsedCCProcessedDependenciesAnnotation.class);
		}

		// This is the coreference link graph
		// Each chain stores a set of mentions that link to each other,
		// along with a method for getting the most representative mention
		// Both sentence and token offsets start at 1!
		Map<Integer, CorefChain> graph = document
				.get(CorefChainAnnotation.class);

		// System.out.println(tagged);

		// System.out.println("[Starting Sentence] " + exampleSentence);
		// for (String sim : similarSentences(exampleSentence)) {
		// System.out.println("[New Sentence]     " + sim);
		// }

		System.out.println("---EnD oF PrOgRaM---");
		*/}
	}

	/**
	 * Given a sentence, this class will return similar sentences, where each
	 * sentence is the same except 1 adjective will be replaced with a synonym
	 * from wordnet.
	 * 
	 * @param sentence
	 * @return new sentences
	 */
	private static ArrayList<String> similarSentences(CoreMap sentence,
			IDictionary wordnetDict) {
		ArrayList<String> similarSentences = new ArrayList<String>();

		HashMap<String, Set<String>> synonyms = new HashMap<String, Set<String>>();

		for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
			// this is the text of the token
			String wordStr = token.get(TextAnnotation.class);
			//System.out.println("wordstr " + wordStr);
			// this is the POS tag of the token
			String pos = token.get(PartOfSpeechAnnotation.class);

			if (getWordnetPOSFromStanfordTag(pos) == POS.ADJECTIVE) {
				HashSet currSet = new HashSet<String>();
				synonyms.put(wordStr, currSet);

				// look up in wordnet for synonyms
				IIndexWord idxWord = wordnetDict.getIndexWord(wordStr,
						getWordnetPOSFromStanfordTag(pos));
				for (int i = 0; i < idxWord.getWordIDs().size(); i++) {
					IWordID wordID = idxWord.getWordIDs().get(i);
					IWord word = wordnetDict.getWord(wordID);
					// synonym set?
					ISynset synset = word.getSynset();
					for (IWord w : synset.getWords()) {
						// synonyms.get(wordStr).add(w.getLemma().toString()
						currSet.add(w.getLemma());

					}

				}

			}

		}

		// test to see we get all the sentences

//		System.out.println("Here are the synonyms for all the adjectives:");
//		for (String adj : synonyms.keySet()) {
//			System.out.print(adj + ": ");
//			for (String syn : synonyms.get(adj)) {
//				System.out.print(syn + ", ");
//			}
//			System.out.println();
//		}

		int numberOfSentencesWeWant = 10;
		for (int numSent = 0; numSent < numberOfSentencesWeWant; numSent++) {
			String currentSentence = "";
			for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
				// this is the text of the token
				String wordStr = token.get(TextAnnotation.class);
				//System.out.println("wordstr " + wordStr);
				// this is the POS tag of the token
				String pos = token.get(PartOfSpeechAnnotation.class);

				

				if (getWordnetPOSFromStanfordTag(pos) == POS.ADJECTIVE) {
					// use a synonym instead
					int index = new Random().nextInt(synonyms.get(wordStr)
							.size());
					int c = 0;
					for (String syn : synonyms.get(wordStr)) {
						if (c == index) {
							currentSentence += " " + syn;
							break;
						}else{
							c++;
						}
					}
					
				} else {
					currentSentence += " " + wordStr;
				}

				
			}
			similarSentences.add(currentSentence);
		}

		return similarSentences;

	}

	/**
	 * Simple mapping function that returns a wordnet POS (Part of Speech) tag
	 * from the stanford tag
	 * 
	 * To-do: returns null if there is no maping for the input tag (i.e. adverb
	 * or article). This should be changed in the future.
	 * 
	 * @param tag
	 * @return POS
	 */
	private static POS getWordnetPOSFromStanfordTag(String tag) {
		switch (tag) {
		case "NN":
			return POS.NOUN;
		case "JJ":
			return POS.ADJECTIVE;
		case "VBG":
			return POS.VERB;
		default:
			return null;
		}
	}

	private static WORDTYPE testDictionary(String originalWord)
			throws IOException {
		// construct the URL to the Wordnet dictionary directory
		String wnhome = System.getenv("WNHOME");
		String path = wnhome + File.separator + "dict";
		System.out.println("Trying path: " + path);
		URL url = new URL("file", null, path);
		// construct the dictionary object and open it
		IDictionary dict = new Dictionary(url);
		dict.open();

		// look up first sense of the word "dog"
		// IIndexWord idxWord = dict.getIndexWord("dog", POS.NOUN);
		IIndexWord idxWord = dict.getIndexWord("dog", POS.NOUN);

		IWordID wordID = idxWord.getWordIDs().get(0);
		IWord word = dict.getWord(wordID);
		System.out.println("Id = " + wordID);
		System.out.println("Lemma = " + word.getLemma());
		System.out.println("Gloss = " + word.getSynset().getGloss());

		return WORDTYPE.ADJECTIVE;
	}

}
