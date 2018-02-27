import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * Classe pour sauvegarder un tableau de données dans un fichier texte
 * @author Richard Moreau, Romain Delpoux, J-François Tregouet
 */

public class Enregistreur {    
    /**
     * Pour effacer le fichier de données s'il existe
     * @param nomFich : le nom du fichier
     */
	public static void effaceFich(String nomFich){
		try {
			File file = new File(nomFich);
			if(!file.exists()){
				throw new Exception("Fichier introuvable");
			}
			if(!file.canWrite())
				throw new Exception("Droit insuffisant");
			file.delete();
            System.out.println("Fichier supprimé");
        } catch (Exception ex) {
            System.out.println("Erreur: "+ex.getMessage());
        }	
	}
	
	/** 
	 * Pour copier un tableau dans un fichier texte
	 * @param nomFich : le nom du fichier
	 * @param t : tableau a memoriser
	 */
	public static void ecrireFich(String nomFich, float[][] t) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(nomFich));
			for(int i=0;i<t.length;i++) {
				for(int j=0;j<t[i].length;j++) {
					writer.write(String.valueOf(t[i][j]));
					writer.write("\t"); // pour ajouter une tabulation entre les donnees
				}
				writer.newLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close(); // Fermeture du flux
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}