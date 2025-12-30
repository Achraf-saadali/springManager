package org.example.springmanager2.Service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
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

import java.io.*;

import java.nio.charset.StandardCharsets;
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
        System.out.println("client...."+client);
        System.out.println("prduitsIdquant"+produitsIdsQuantites);
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
        System.out.println("creating facture1......"+facture);

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
        System.out.println("creating facture2......"+facture);
        facture.setEtat(Etat.ATTENTE);

        // 🔥 VERY IMPORTANT
        for (LigneFacture ligne : lignes) {
            // 🔥 VERY IMPORTANT
            Produit managedProduit = produitRepository
                    .findById(ligne.getProduit().getIdProduit())
                    .orElseThrow(() -> new IllegalArgumentException("Produit introuvable"));

            ligne.setProduit(managedProduit);
            ligne.setFacture(facture);
        }
        System.out.println("les lignes "+lignes);


  // ce code ici je ne pense pas qu il marche car tout le code apres ne s affiche jamais
        facture.setProduitCommandes(lignes);
        System.out.println("creating facture3......"+facture);

        double total = lignes.stream()
                .mapToDouble(l -> l.getQuantiteCommandes() * l.getProduit().getPrixUnitaire())
                .sum();

        facture.setPrixFacture(total);
        System.out.println("creating facture4......"+facture);
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

        // Prepare products (SAME LOGIC)
        List<Map<String, Object>> produitsData = facture.getProduitCommandes().stream()
                .map(ligne -> {
                    Map<String, Object> data = new HashMap<>();
                    data.put("product_id", ligne.getProduit().getIdProduit());
                    data.put("product_name", ligne.getProduit().getName());
                    data.put("amount", ligne.getQuantiteCommandes());
                    data.put("unit_price", ligne.getProduit().getPrixUnitaire());
                    data.put("line_total",
                            ligne.getQuantiteCommandes() * ligne.getProduit().getPrixUnitaire());
                    return data;
                })
                .collect(Collectors.toList());

        StringBuilder rows = new StringBuilder();
        for (Map<String, Object> p : produitsData) {
            rows.append("<tr>")
                    .append("<td>").append(p.get("product_id")).append("</td>")
                    .append("<td>").append(p.get("product_name")).append("</td>")
                    .append("<td>").append(p.get("amount")).append("</td>")
                    .append("<td>").append(p.get("unit_price")).append("</td>")
                    .append("<td>").append(p.get("line_total")).append("</td>")
                    .append("</tr>");
        }

        // SAFE XHTML HTML (NO <br>, NO <meta>, NO VOID TAGS)
        String html =
                "<!DOCTYPE html>" +
                        "<html>" +
                        "<head>" +
                        "<meta charset='UTF-8'/>" +
                        "<title>Invoice - " + COMPANY_NAME + "</title>" +
                        "<style>\n" +
                        "    body {\n" +
                        "      font-family: Arial, sans-serif;\n" +
                        "      margin: 0;\n" +
                        "      padding: 20pt;\n" +
                        "      background-color: #f8fafc;\n" +
                        "      color: #1e293b;\n" +
                        "    }\n" +
                        "\n" +
                        "    .invoice {\n" +
                        "      max-width: 850px;\n" +
                        "      margin: 0 auto;\n" +
                        "      background: #ffffff;\n" +
                        "      border: 1pt solid #e2e8f0;\n" +
                        "    }\n" +
                        "\n" +
                        "    /* Header - Using TABLE instead of flexbox */\n" +
                        "    .header {\n" +
                        "      background-color: #1e3a5f;\n" +
                        "      color: #ffffff;\n" +
                        "      padding: 25pt;\n" +
                        "    }\n" +
                        "\n" +
                        "    .header table {\n" +
                        "      width: 100%;\n" +
                        "      border-collapse: collapse;\n" +
                        "    }\n" +
                        "\n" +
                        "    .company-name {\n" +
                        "      font-size: 18pt;\n" +
                        "      font-weight: bold;\n" +
                        "      margin-bottom: 5pt;\n" +
                        "    }\n" +
                        "\n" +
                        "    .company-tagline {\n" +
                        "      font-size: 10pt;\n" +
                        "      color: #cbd5e1;\n" +
                        "    }\n" +
                        "\n" +
                        "    .invoice-title {\n" +
                        "      font-size: 24pt;\n" +
                        "      font-weight: normal;\n" +
                        "      margin-bottom: 10pt;\n" +
                        "      text-align: right;\n" +
                        "    }\n" +
                        "\n" +
                        "    .invoice-meta {\n" +
                        "      font-size: 10pt;\n" +
                        "      text-align: right;\n" +
                        "      line-height: 1.6;\n" +
                        "    }\n" +
                        "\n" +
                        "    /* Billing Section - Using TABLE */\n" +
                        "    .billing-section {\n" +
                        "      padding: 20pt 25pt;\n" +
                        "      border-bottom: 1pt solid #f1f5f9;\n" +
                        "    }\n" +
                        "\n" +
                        "    .billing-section table {\n" +
                        "      width: 100%;\n" +
                        "      border-collapse: collapse;\n" +
                        "    }\n" +
                        "\n" +
                        "    .billing-block h3 {\n" +
                        "      font-size: 9pt;\n" +
                        "      font-weight: bold;\n" +
                        "      color: #64748b;\n" +
                        "      text-transform: uppercase;\n" +
                        "      margin-bottom: 10pt;\n" +
                        "    }\n" +
                        "\n" +
                        "    .billing-block .name {\n" +
                        "      font-size: 13pt;\n" +
                        "      font-weight: bold;\n" +
                        "      color: #1e3a5f;\n" +
                        "      margin-bottom: 5pt;\n" +
                        "    }\n" +
                        "\n" +
                        "    .billing-block .address {\n" +
                        "      font-size: 10pt;\n" +
                        "      color: #64748b;\n" +
                        "      line-height: 1.6;\n" +
                        "    }\n" +
                        "\n" +
                        "    /* Products Table */\n" +
                        "    .products-section {\n" +
                        "      padding: 20pt 25pt;\n" +
                        "    }\n" +
                        "\n" +
                        "    .products-table {\n" +
                        "      width: 100%;\n" +
                        "      border-collapse: collapse;\n" +
                        "    }\n" +
                        "\n" +
                        "    .products-table thead tr {\n" +
                        "      background-color: #1e3a5f;\n" +
                        "      color: #ffffff;\n" +
                        "    }\n" +
                        "\n" +
                        "    .products-table th {\n" +
                        "      padding: 10pt 12pt;\n" +
                        "      font-size: 9pt;\n" +
                        "      font-weight: bold;\n" +
                        "      text-transform: uppercase;\n" +
                        "      text-align: left;\n" +
                        "    }\n" +
                        "\n" +
                        "    .products-table th.text-center {\n" +
                        "      text-align: center;\n" +
                        "    }\n" +
                        "\n" +
                        "    .products-table th.text-right {\n" +
                        "      text-align: right;\n" +
                        "    }\n" +
                        "\n" +
                        "    .products-table td {\n" +
                        "      padding: 12pt;\n" +
                        "      border-bottom: 1pt solid #f1f5f9;\n" +
                        "    }\n" +
                        "\n" +
                        "    .product-name {\n" +
                        "      font-weight: bold;\n" +
                        "      color: #1e3a5f;\n" +
                        "    }\n" +
                        "\n" +
                        "    .product-desc {\n" +
                        "      font-size: 10pt;\n" +
                        "      color: #64748b;\n" +
                        "      margin-top: 3pt;\n" +
                        "    }\n" +
                        "\n" +
                        "    .products-table td.text-center {\n" +
                        "      text-align: center;\n" +
                        "      color: #475569;\n" +
                        "    }\n" +
                        "\n" +
                        "    .products-table td.text-right {\n" +
                        "      text-align: right;\n" +
                        "      color: #475569;\n" +
                        "    }\n" +
                        "\n" +
                        "    .products-table td.total {\n" +
                        "      font-weight: bold;\n" +
                        "      color: #1e3a5f;\n" +
                        "    }\n" +
                        "\n" +
                        "    /* Summary Section - Using TABLE */\n" +
                        "    .summary-section {\n" +
                        "      padding: 20pt 25pt;\n" +
                        "      background-color: #f8fafc;\n" +
                        "    }\n" +
                        "\n" +
                        "    .summary-section table {\n" +
                        "      width: 100%;\n" +
                        "      border-collapse: collapse;\n" +
                        "    }\n" +
                        "\n" +
                        "    .qr-code {\n" +
                        "      width: 110pt;\n" +
                        "      height: 110pt;\n" +
                        "      background-color: #e2e8f0;\n" +
                        "      text-align: center;\n" +
                        "      vertical-align: middle;\n" +
                        "      border: 1pt solid #cbd5e1;\n" +
                        "    }\n" +
                        "\n" +
                        "    .qr-text {\n" +
                        "      padding-left: 15pt;\n" +
                        "    }\n" +
                        "\n" +
                        "    .qr-text h4 {\n" +
                        "      font-weight: bold;\n" +
                        "      color: #1e3a5f;\n" +
                        "      margin: 0 0 5pt 0;\n" +
                        "    }\n" +
                        "\n" +
                        "    .qr-text p {\n" +
                        "      font-size: 10pt;\n" +
                        "      color: #64748b;\n" +
                        "      margin: 0;\n" +
                        "    }\n" +
                        "\n" +
                        "    .totals {\n" +
                        "      text-align: right;\n" +
                        "      vertical-align: top;\n" +
                        "    }\n" +
                        "\n" +
                        "    .totals-row {\n" +
                        "      margin-bottom: 8pt;\n" +
                        "      color: #475569;\n" +
                        "    }\n" +
                        "\n" +
                        "    .totals-row .value {\n" +
                        "      font-weight: bold;\n" +
                        "      padding-left: 20pt;\n" +
                        "    }\n" +
                        "\n" +
                        "    .totals-divider {\n" +
                        "      border-top: 1pt solid #e2e8f0;\n" +
                        "      margin: 10pt 0;\n" +
                        "    }\n" +
                        "\n" +
                        "    .totals-total {\n" +
                        "      font-size: 16pt;\n" +
                        "      font-weight: bold;\n" +
                        "      color: #1e3a5f;\n" +
                        "      margin-top: 10pt;\n" +
                        "    }\n" +
                        "\n" +
                        "    /* Footer */\n" +
                        "    .footer {\n" +
                        "      padding: 20pt 25pt;\n" +
                        "      border-top: 1pt solid #f1f5f9;\n" +
                        "      text-align: center;\n" +
                        "    }\n" +
                        "\n" +
                        "    .footer-message {\n" +
                        "      font-size: 13pt;\n" +
                        "      font-weight: bold;\n" +
                        "      color: #1e3a5f;\n" +
                        "      margin-bottom: 5pt;\n" +
                        "    }\n" +
                        "\n" +
                        "    .footer-note {\n" +
                        "      font-size: 10pt;\n" +
                        "      color: #64748b;\n" +
                        "      margin-bottom: 15pt;\n" +
                        "    }\n" +
                        "\n" +
                        "    .footer-contact {\n" +
                        "      font-size: 10pt;\n" +
                        "      color: #64748b;\n" +
                        "    }\n" +
                        "  </style>" +
                        "</head>" +
                        "<body>" +

                        "<div class='invoice'>" +

                        "<!-- Header using table -->" +
                        "<div class='header'>" +
                        "<table>" +
                        "<tr>" +
                        "<td style='width: 50%; vertical-align: top;'>" +
                        "<div class='company-name'>" + COMPANY_NAME + "</div>" +
                        "<div class='company-tagline'>Premium E-Commerce Solutions</div>" +
                        "</td>" +
                        "<td style='width: 50%; vertical-align: top;'>" +
                        "<div class='invoice-title'>INVOICE</div>" +
                        "<div class='invoice-meta'>" +
                        "<div>Invoice No: FAC-" + facture.getIdFacture() + "</div>" +
                        "<div>Date: " + new SimpleDateFormat("dd/MM/yyyy").format(facture.getCreatedAt()) + "</div>" +
                        "<div>Due Date: TBD</div>" +
                        "</div>" +
                        "</td>" +
                        "</tr>" +
                        "</table>" +
                        "</div>" +

                        "<!-- Billing Section using table -->" +
                        "<div class='billing-section'>" +
                        "<table>" +
                        "<tr>" +
                        "<td style='width: 50%; vertical-align: top;'>" +
                        "<div class='billing-block'>" +
                        "<h3>Bill To</h3>" +
                        "<div class='name'>" + facture.getCode() + "</div>" +
                        "<div class='address'>" + "Gare Ain sebaa Casablanca".replace("\n","<br/>") + "</div>" +
                        "</div>" +
                        "</td>" +
                        "<td style='width: 50%; vertical-align: top;'>" +
                        "<div class='billing-block'>" +
                        "<h3>Ship To</h3>" +
                        "<div class='name'>" + facture.getCode() + "</div>" +
                        "<div class='address'>" + COMPANY_NAME + "</div>" +
                        "</div>" +
                        "</td>" +
                        "</tr>" +
                        "</table>" +
                        "</div>" +

                        "<!-- Products Table -->" +
                        "<div class='products-section'>" +
                        "<table class='products-table'>" +
                        "<thead>" +
                        "<tr>" +
                        "<th>Description</th>" +
                        "<th class='text-center'>Qty</th>" +
                        "<th class='text-right'>Unit Price</th>" +
                        "<th class='text-right'>Total</th>" +
                        "</tr>" +
                        "</thead>" +
                        "<tbody>" +
                        rows +
                        "</tbody>" +
                        "</table>" +
                        "</div>" +

                        "<!-- Summary Section using table -->" +
                        "<div class='summary-section'>" +
                        "<table>" +
                        "<tr>" +
                        "<td style='width: 50%; vertical-align: top;'>" +
                        "<table>" +
                        "<tr>" +
                        "<td class='qr-code'>QR</td>" +
                        "<td class='qr-text'>" +
                        "<h4>Scan to Pay</h4>" +
                        "<p>Scan this QR code with your<br/>banking app for instant payment</p>" +
                        "</td>" +
                        "</tr>" +
                        "</table>" +
                        "</td>" +
                        "<td style='width: 50%; vertical-align: top;'>" +
                        "<div class='totals'>" +
                        "<div class='totals-row'>Tax: <span class='value'>1.2%</span></div>" +
                        "<div class='totals-divider'></div>" +
                        "<div class='totals-total'>Total Due: " + facture.getPrixFacture() + " DH</div>" +
                        "</div>" +
                        "</td>" +
                        "</tr>" +
                        "</table>" +
                        "</div>" +

                        "<!-- Footer -->" +
                        "<div class='footer'>" +
                        "<div class='footer-message'>Thank you for your business!</div>" +
                        "<div class='footer-note'>Payment is due within 14 days of invoice date.</div>" +
                        "<div class='footer-contact'>" +
                        "Email: " + COMPANY_EMAIL + " | Phone: " + COMPANY_PHONE +
                        "</div>" +
                        "</div>" +

                        "</div>" +
                        "</body></html>";



        // HTML → PDF
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, null);
            builder.toStream(out);
            builder.run();
            return out.toByteArray();
        }
    }



    private String populateHtmlTemplate(String template, Map<String, Object> params) {

        String html = template;

        // Simple variables
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (!(entry.getValue() instanceof List)) {
                html = html.replace(
                        "{{" + entry.getKey() + "}}",
                        String.valueOf(entry.getValue())
                );
            }
        }

        // Products table
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> produits =
                (List<Map<String, Object>>) params.get("produits");

        StringBuilder rows = new StringBuilder();
        for (Map<String, Object> p : produits) {
            rows.append("<tr>")
                    .append("<td>").append(p.get("product_id")).append("</td>")
                    .append("<td>").append(p.get("product_name")).append("</td>")
                    .append("<td>").append(p.get("amount")).append("</td>")
                    .append("<td>").append(p.get("unit_price")).append("</td>")
                    .append("<td>").append(p.get("line_total")).append("</td>")
                    .append("</tr>");
        }

        html = html.replace("{{products_rows}}", rows.toString());

        return html;
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
//            System.err.println("   1. Le fichier 'facture_template.html' est-il dans src/main/resources ?");
//            System.err.println("   2. Les dépendances JasperReports sont-elles dans le classpath ?");
//            System.err.println("   3. Y a-t-il assez d'espace disque ?");
//        }
//    }
}