package org.example.springmanager2.Service;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.example.springmanager2.Entity.*;
import org.example.springmanager2.Entity.Enums.Etat;
import org.example.springmanager2.Entity.Enums.ROLES;
import org.example.springmanager2.Repository.ClientRepo;
import org.example.springmanager2.Repository.ComptableRepo;
import org.example.springmanager2.Repository.FactureRepo;
import org.example.springmanager2.Repository.ProduitRepo;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class FactureService {

    private final FactureRepo factureRepository;
    private final ProduitRepo produitRepository;
    private final ClientRepo clientRepository;
    private final ComptableRepo comptableRepository;

    // ============================================
    // INFOS ENTREPRISE FIXES
    // ============================================
    private static final String COMPANY_NAME = "BASE";
    private static final String COMPANY_EMAIL = "group9@gmail.com";
    private static final String COMPANY_PHONE = "0666666666";
    private static final String COMPANY_ADDRESS = "ENSA El Jadida, El Jadida, Maroc";

    public FactureService(FactureRepo factureRepository,
                          ProduitRepo produitRepository,
                          ClientRepo clientRepository,
                          ComptableRepo comptableRepository) {
        this.factureRepository = factureRepository;
        this.produitRepository = produitRepository;
        this.clientRepository = clientRepository;
        this.comptableRepository = comptableRepository;
    }

    public Facture creerFactureClient(String code, List<LigneFacture> produitsIdsQuantites) {
        Client client = clientRepository.findByClientCode(code);
        return creerFacture(client, produitsIdsQuantites);
    }

    public List<Facture> findByTypeFacture(ROLES typeFacture) {
        return factureRepository.findByTypeFacture(typeFacture);
    }


    public Facture creerFactureComptable(String code,List<LigneFacture> produitsIdsQuantites) {
        Comptable comptable = comptableRepository.findByComptableCode(code);
        return creerFacture(comptable, produitsIdsQuantites);
    }
    @Transactional
    private Facture creerFacture(Personne personne, List<LigneFacture> lignes) {

        if (personne == null) {
            throw new IllegalArgumentException("Personne invalide");
        }

        Facture facture = new Facture();

        switch (personne) {
            case Client c -> {
                facture.setTypeFacture(ROLES.CLIENT);
                facture.setCode(c.getClientCode());
            }
            case Comptable c -> {
                facture.setTypeFacture(ROLES.COMPTABLE);
                facture.setCode(c.getComptableCode());
            }
            default -> throw new IllegalArgumentException("Personne invalide");
        }

        facture.setEtat(Etat.ATTENTE);

        // 🔥 VERY IMPORTANT
        for (LigneFacture ligne : lignes) {
            ligne.setFacture(facture);
        }

        facture.setProduitCommandes(lignes);

        double total = lignes.stream()
                .mapToDouble(l -> l.getQuantiteCommandes() * l.getProduit().getPrixUnitaire())
                .sum();

        facture.setPrixFacture(total);

        return factureRepository.save(facture);
    }

    public List<Facture> getFacturesClient(String codeClient) {

        return factureRepository.findByCodeAndTypeFacture(codeClient, ROLES.CLIENT);
    }

    public List<Facture> getFacturesComptable(String codeComptable) {
        return factureRepository.findByCodeAndTypeFacture(codeComptable, ROLES.COMPTABLE);
    }
    public List<Facture> findByCodeAndTypeFacture(String code, ROLES typeFacture)
    {
        return factureRepository.findByCodeAndTypeFacture(code,typeFacture);
    }

    // ============================================
    // GÉNÉRATION PDF AVEC JASPER REPORTS
    // ============================================
    public byte[] genererPDF(Facture facture) throws Exception {
        // Charger le template JRXML
        Resource resource = new ClassPathResource("facture_template.jrxml");
        InputStream jrxmlStream = resource.getInputStream();

        // Compiler le rapport
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlStream);

        // Préparer les données pour le tableau des produits
        List<Map<String, Object>> produitsData = facture.getProduitCommandes().stream()
                .map(ligne -> {
                    Map<String, Object> data = new HashMap<>();
                    data.put("product_id", ligne.getProduit().getIdProduit());
                    data.put("product_name", ligne.getProduit().getName());
                    data.put("amount", ligne.getQuantiteCommandes());
                    data.put("unit_price", ligne.getProduit().getPrixUnitaire());
                    data.put("line_total", ligne.getQuantiteCommandes() * ligne.getProduit().getPrixUnitaire());
                    return data;
                })
                .collect(Collectors.toList());

        // Créer une source de données Bean
        JRDataSource dataSource = new JRBeanCollectionDataSource(produitsData);

        // Paramètres pour le rapport
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("companyName", COMPANY_NAME);
        parameters.put("companyAddress", COMPANY_ADDRESS);
        parameters.put("companyPhone", COMPANY_PHONE);
        parameters.put("companyEmail", COMPANY_EMAIL);
        parameters.put("invoiceNumber", "FAC-" + facture.getIdFacture());
        parameters.put("invoiceDate", new SimpleDateFormat("dd/MM/yyyy").format(facture.getCreatedAt()));
        parameters.put("clientCode", facture.getCode());
        parameters.put("invoiceType", facture.getTypeFacture().toString());
        parameters.put("totalAmount", facture.getPrixFacture());

        // Remplir le rapport avec les données
        JasperPrint jasperPrint = JasperFillManager.fillReport(
                jasperReport,
                parameters,
                dataSource
        );

        // Exporter en PDF
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    // Méthode pour sauvegarder le PDF dans un fichier
    public void genererEtSauvegarderPDF(Facture facture, String cheminFichier) throws Exception {
        byte[] pdfBytes = genererPDF(facture);
        java.nio.file.Files.write(
                java.nio.file.Paths.get(cheminFichier),
                pdfBytes
        );
    }
    public Facture findById(Integer idFacture)
    {
        return factureRepository.findById(idFacture).orElse(null);
    }

    // ============================================
    // MAIN POUR TESTER LA GÉNÉRATION DE PDF
    // ============================================
//    public static void main(String[] args) {
//        System.out.println("╔══════════════════════════════════════════════════════════════════╗");
//        System.out.println("║                    🧪 TEST GÉNÉRATION PDF FACTURE               ║");
//        System.out.println("╚══════════════════════════════════════════════════════════════════╝\n");
//
//        try {
//            // Créer des produits de test
//            Produit p1 = new Produit();
//            p1.setIdProduit(101);
//            p1.setName("Ordinateur Portable HP EliteBook");
//            p1.setPrixUnitaire(1599.99);
//            p1.setQuantiteStock(15);
//
//            Produit p2 = new Produit();
//            p2.setIdProduit(102);
//            p2.setName("Souris Sans Fil Logitech MX Master 3");
//            p2.setPrixUnitaire(89.99);
//            p2.setQuantiteStock(50);
//
//            Produit p3 = new Produit();
//            p3.setIdProduit(103);
//            p3.setName("Clavier Mécanique Razer BlackWidow");
//            p3.setPrixUnitaire(129.99);
//            p3.setQuantiteStock(25);
//
//            Produit p4 = new Produit();
//            p4.setIdProduit(104);
//            p4.setName("Écran 27\" 4K Samsung U28R550");
//            p4.setPrixUnitaire(349.99);
//            p4.setQuantiteStock(10);
//
//            System.out.println("✅ Produits créés pour le test:");
//            System.out.printf("   1. %-40s %8.2f €\n", p1.getName(), p1.getPrixUnitaire());
//            System.out.printf("   2. %-40s %8.2f €\n", p2.getName(), p2.getPrixUnitaire());
//            System.out.printf("   3. %-40s %8.2f €\n", p3.getName(), p3.getPrixUnitaire());
//            System.out.printf("   4. %-40s %8.2f €\n\n", p4.getName(), p4.getPrixUnitaire());
//
//            // Créer des lignes de facture
//            List<LigneFacture> lignes = new ArrayList<>();
//
//            LigneFacture l1 = new LigneFacture();
//            l1.setProduit(p1);
//            l1.setQuantiteCommandes(2);
//
//            LigneFacture l2 = new LigneFacture();
//            l2.setProduit(p2);
//            l2.setQuantiteCommandes(3);
//
//            LigneFacture l3 = new LigneFacture();
//            l3.setProduit(p3);
//            l3.setQuantiteCommandes(1);
//
//            LigneFacture l4 = new LigneFacture();
//            l4.setProduit(p4);
//            l4.setQuantiteCommandes(1);
//
//            lignes.add(l1);
//            lignes.add(l2);
//            lignes.add(l3);
//            lignes.add(l4);
//
//            // Calculer le total
//            double total = lignes.stream()
//                    .mapToDouble(l -> l.getQuantiteCommandes() * l.getProduit().getPrixUnitaire())
//                    .sum();
//
//            // Afficher le détail
//            System.out.println("📋 Détail des lignes de facture:");
//            System.out.println("   ┌─────┬──────────────────────────────────────┬──────┬──────────┬──────────┐");
//            System.out.println("   │ N°  │ Produit                              │ Qté  │ Prix U.  │ Total    │");
//            System.out.println("   ├─────┼──────────────────────────────────────┼──────┼──────────┼──────────┤");
//
//            int i = 1;
//            for (LigneFacture l : lignes) {
//                double ligneTotal = l.getQuantiteCommandes() * l.getProduit().getPrixUnitaire();
//                System.out.printf("   │ %-3d │ %-36s │ %-4d │ %7.2f € │ %7.2f € │\n",
//                        i++,
//                        l.getProduit().getName().substring(0, Math.min(36, l.getProduit().getName().length())),
//                        l.getQuantiteCommandes(),
//                        l.getProduit().getPrixUnitaire(),
//                        ligneTotal);
//            }
//            System.out.println("   └─────┴──────────────────────────────────────┴──────┴──────────┴──────────┘\n");
//
//            System.out.printf("💰 Total HT: %.2f €\n", total);
//            System.out.printf("💰 TVA (20%%): %.2f €\n", total * 0.20);
//            System.out.printf("💰 Total TTC: %.2f €\n\n", total * 1.20);
//
//            // Créer une facture de test
//            Facture facture = new Facture();
//            facture.setIdFacture(2024001);
//            facture.setCode("CLIENT-789");
//            facture.setCreatedAt(new java.sql.Date(System.currentTimeMillis()));
//            facture.setTypeFacture(ROLES.CLIENT);
//            facture.setEtat(Etat.ATTENTE);
//            facture.setPrixFacture(total);
//            facture.setProduitCommandes(lignes);
//
//            // Lier la facture aux lignes
//            for (LigneFacture l : lignes) {
//                l.setFacture(facture);
//            }
//
//            System.out.println("✅ Facture de test créée:");
//            System.out.println("   • ID Facture: " + facture.getIdFacture());
//            System.out.println("   • Code Client: " + facture.getCode());
//            System.out.println("   • Date: " + new SimpleDateFormat("dd/MM/yyyy").format(facture.getCreatedAt()));
//            System.out.println("   • Type: " + facture.getTypeFacture());
//            System.out.println("   • État: " + facture.getEtat() + "\n");
//
//            // Créer une instance du service pour le test (avec null pour les repositories non utilisés)
//            FactureService service = new FactureService(null, null, null, null);
//
//            // Générer le PDF
//            String nomFichier = "facture_test_" + facture.getIdFacture() + ".pdf";
//            service.genererEtSauvegarderPDF(facture, nomFichier);
//
//            // Vérifier si le fichier a été créé
//            java.io.File fichierPDF = new java.io.File(nomFichier);
//
//            if (fichierPDF.exists()) {
//                System.out.println("╔══════════════════════════════════════════════════════════════════╗");
//                System.out.println("║                      ✅ TEST RÉUSSI !                          ║");
//                System.out.println("╠══════════════════════════════════════════════════════════════════╣");
//                System.out.printf("║ 📄 Fichier généré: %-40s ║\n", nomFichier);
//                System.out.printf("║ 📁 Chemin complet: %-40s ║\n", fichierPDF.getAbsolutePath());
//                System.out.printf("║ 📏 Taille: %-45d octets ║\n", fichierPDF.length());
//                System.out.println("╚══════════════════════════════════════════════════════════════════╝\n");
//
//                System.out.println("📁 Emplacement du fichier:");
//                System.out.println("   " + fichierPDF.getAbsolutePath());
//
//                // Ouvrir le fichier automatiquement si sur Windows
//                try {
//                    if (System.getProperty("os.name").toLowerCase().contains("win")) {
//                        java.awt.Desktop.getDesktop().open(fichierPDF);
//                        System.out.println("\n📂 Ouverture automatique du PDF...");
//                    }
//                } catch (Exception e) {
//                    System.out.println("\nℹ️  Pour visualiser le PDF: double-cliquez sur le fichier.");
//                }
//
//            } else {
//                System.out.println("❌ ERREUR: Le fichier PDF n'a pas été créé.");
//            }
//
//        } catch (Exception e) {
//            System.err.println("\n❌ ERREUR lors du test:");
//            System.err.println("   Message: " + e.getMessage());
//            System.err.println("   Cause: " + e.getCause());
//            e.printStackTrace();
//
//            System.err.println("\n🔧 Vérifications:");
//            System.err.println("   1. Le fichier 'facture_template.jrxml' est-il dans src/main/resources ?");
//            System.err.println("   2. Les dépendances JasperReports sont-elles dans le classpath ?");
//            System.err.println("   3. Y a-t-il assez d'espace disque ?");
//        }
//    }
}