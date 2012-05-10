import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Random;
import javax.imageio.ImageIO;
import weka.attributeSelection.BestFirst;
import weka.attributeSelection.CfsSubsetEval;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.RBFNetwork;
import weka.classifiers.meta.AdaBoostM1;
import weka.core.*;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.unsupervised.attribute.Remove;

public class HelloWeka {

	public static Instances mergeInstances(Instances st, Instances nd) {
		Instances all = new Instances(st);
		for (int i = 0; i < nd.numInstances(); ++i) {
			all.add(nd.instance(i));
		}
		return all;
	}

	public static Instances attributeSelection(Instances data, int[] features)
			throws Exception {
		features[features.length - 1] = data.numAttributes() - 1;
		Remove rm = new Remove();
		rm.setAttributeIndicesArray(features);
		rm.setInvertSelection(true);
		rm.setInputFormat(data);

		return Filter.useFilter(data, rm);
	}

	public static Instances attributeSelection(Instances data) throws Exception {
		// select atributes
		AttributeSelection filter = new AttributeSelection(); // package
																// weka.filters.supervised.attribute!
		CfsSubsetEval evall = new CfsSubsetEval();
		BestFirst search = new BestFirst();
		search.setDirection(new SelectedTag(2, BestFirst.TAGS_SELECTION));
		filter.setEvaluator(evall);
		filter.setSearch(search);

		filter.setInputFormat(data);
		return Filter.useFilter(data, filter);

	}

	public static Instances readInstances(File[] files) {
		Instances ret = null;
		// Declare the class attribute along with its values
		FastVector fvClassVal = new FastVector(3);
		fvClassVal.addElement("positive0"); //zakaz
		fvClassVal.addElement("positive1"); //odwo�anie ograniczenia
		fvClassVal.addElement("negative"); //ka�dy inny
		Attribute ClassAttribute = new Attribute("theClass", fvClassVal);

		try {

			BufferedImage img = ImageIO.read(files[0]);
			FastVector vec = new FastVector(img.getHeight() * img.getWidth() + 1);
			for (int i = 0; i < img.getHeight() * img.getWidth(); ++i)
				vec.addElement(new Attribute("attr" + i));
			
			vec.addElement(ClassAttribute);
			
			System.out.println(vec.size());
			
			ret = new Instances("data", vec, 0);

			for (int i = 0; i < files.length; ++i) {
				img = ImageIO.read(files[i]);
				Instance ince = new Instance(img.getHeight() * img.getWidth() + 1);

				int val = 0;
				for (int j = 0; j < img.getWidth(); ++j) {
					for (int k = 0; k < img.getHeight(); ++k) {
						ince.setValue((Attribute) vec.elementAt(val),(double) img.getRGB(j, k));
						val++;
					}
				}
				
				if (files[i].getName().contains("ozakaz")) //
					ince.setValue((Attribute) vec.elementAt(val), "positive1");
				else if (files[i].getName().contains("zakaz")) //
					ince.setValue((Attribute) vec.elementAt(val), "positive0");
				else
					ince.setValue((Attribute) vec.elementAt(val), "negative");

				
				ret.add(ince);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return ret;
	}

	public static void main(String[] args) {

		if (args.length < 2) {
			System.err.println("Bad arguments\n" + "classifier (1|2)"
					+ "directory/ (train data *.png)");
			return;
		}

		FilenameFilter pngfilter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				String[] namesplit = name.split("\\.");
				return namesplit[namesplit.length - 1].equalsIgnoreCase("png");
			}
		};

		File trainf = new File(args[1]), testf = null;
		if (args.length > 2 && args[2] != null)
			testf = new File(args[2]);

		File[] train_files = null, test_files = null;

		if (trainf.isDirectory())
			train_files = trainf.listFiles(pngfilter);
		else
			System.err.println("given path is not a directory:" + args[1]);

		if (testf != null)
			if (testf.isDirectory())
				test_files = testf.listFiles(pngfilter);
			else
				System.err.println("given path is not a directory:" + args[2]);

		Classifier classify = null;
		int[] features = null;
		
		int method = Integer.parseInt(args[0]);
		if (method == 1) {
			// tutaj MLP
			classify = new MultilayerPerceptron();
			((MultilayerPerceptron) classify).setAutoBuild(true);
		} else if (method == 2) {
			// tutaj RBFN
			classify = new RBFNetwork();
			((RBFNetwork) classify).setNumClusters(5);
		} else {
			
			try {
				
				classify = (Classifier) SerializationHelper.read("learned.model");
				ObjectInputStream oi = new ObjectInputStream(new BufferedInputStream(
						new FileInputStream("learned.features")));
				features = (int[]) oi.readObject();
				oi.close();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		Instances training_data = readInstances(train_files);
		training_data.setClassIndex(training_data.numAttributes() - 1);

		Instances test_data = null;
		if (testf != null) {
			test_data = readInstances(test_files);
			test_data.setClassIndex(test_data.numAttributes() - 1);
		}

		Random rand = new Random(1);

		try {

			if (features != null) {
				training_data = attributeSelection(training_data, features);
				test_data = attributeSelection(test_data, features);
			} else {
				//tylko uczymy, test_data sie nie przyda
				training_data = HelloWeka.attributeSelection(training_data);
			}

			for (int i = 0; i < training_data.numAttributes(); ++i)
				System.out.println(training_data.attribute(i).name());

			training_data.randomize(rand);
			System.out.println(training_data.numAttributes());

			int folds = 5;

			if (method < 3) {

				Instances predictedData = null;
				Evaluation eval = new Evaluation(training_data);
				
//				Bagging bg = new Bagging();
//				bg.setClassifier(classify);
//				bg.buildClassifier(training_data);
				
				AdaBoostM1 adaboost = new AdaBoostM1();
				adaboost.setClassifier(classify);
				adaboost.buildClassifier(training_data);
				
				classify = adaboost;
				
				eval.crossValidateModel(classify, training_data, folds, rand);
				
				SerializationHelper.write("learned.model", classify);

				features = new int[training_data.numAttributes()];
				for (int i = 0; i < training_data.numAttributes() - 1; ++i) {
					features[i] = Integer.parseInt(training_data.instance(0).attribute(i).name().replaceAll("[^0-9]", ""));
				}
				features[training_data.numAttributes() - 1] = training_data.numAttributes() - 1;

				ObjectOutputStream oo = new ObjectOutputStream(
						new BufferedOutputStream(new FileOutputStream("learned.features")));
				oo.writeObject(features);
				oo.flush();
				oo.close();

				System.out.println(eval.toSummaryString());
				System.out.println(eval.toClassDetailsString());
				
			} else {
				
				// classify data with learned model
				
				Evaluation eTest = new Evaluation(test_data);
				eTest.evaluateModel(classify, test_data);

				Instances labeled = new Instances(test_data);
				int missed = 0;
				// label instances
				for (int i = 0; i < test_data.numInstances(); i++) {
					
					
					double clsLabel = classify.classifyInstance(test_data.instance(i));
					labeled.instance(i).setClassValue(clsLabel);
					
					
					System.out.println(i + ": label :"
							+ test_data.instance(i).classValue()
							+ ":classified as:"
							+ labeled.instance(i).classValue());
					
					if (test_data.instance(i).classValue() != labeled.instance(i).classValue())
						missed++;
				}
				
				
				System.out.println("Accuracy: "
						+ (100 - 100.d * (double) missed
								/ (double) test_data.numInstances()) + "%");

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
