package paysdumonde;

import java.util.HashMap;
import java.util.Random;

public class GetPaysRandom {
	private static HashMap<String,String> tab;
	private static void initHash() {
		tab = new HashMap<String,String>();

		tab.put("Afghanistan","Kaboul");
		tab.put("Afrique du Sud","Pretoria (exécutive), Bloemfontein (judiciaire), Le Cap (législative)");
		tab.put("Albanie","Tirana");
		tab.put("Algérie","Alger");
		tab.put("Allemagne","Berlin");
		tab.put("Andorre","Andorre-la-Vieille");
		tab.put("Angola","Luanda");
		tab.put("Antigua-et-Barbuda","Saint John's");
		tab.put("Arabie saoudite","Riyad");
		tab.put("Argentine","Buenos Aires");
		tab.put("Arménie","Erevan");
		tab.put("Australie","Canberra");
		tab.put("Autriche","Vienne");
		tab.put("Azerbaïdjan","Bakou");
		tab.put("Bahamas","Nassau");
		tab.put("Bahreïn","Manama");
		tab.put("Bangladesh","Dacca");
		tab.put("Barbade","Bridgetown");
		tab.put("Bélarus (Biélorussie)","Minsk");
		tab.put("Belgique","Bruxelles");
		tab.put("Belize","Belmopan");
		tab.put("Bénin","Porto-Novo (exécutive), Cotonou (législative)");
		tab.put("Bhoutan","Thimphou");
		tab.put("Birmanie (Myanmar)","Naypyidaw");
		tab.put("Bolivie","Sucre (constitutionnelle), La Paz (siège du gouvernement)");
		tab.put("Bosnie-Herzégovine","Sarajevo");
		tab.put("Botswana","Gaborone");
		tab.put("Brésil","Brasília");
		tab.put("Brunei","Bandar Seri Begawan");
		tab.put("Bulgarie","Sofia");
		tab.put("Burkina Faso","Ouagadougou");
		tab.put("Burundi","Bujumbura");
		tab.put("Cambodge","Phnom Penh");
		tab.put("Cameroun","Yaoundé");
		tab.put("Canada","Ottawa");
		tab.put("Cap-Vert","Praia");
		tab.put("Chili","Santiago");
		tab.put("Chine","Pékin");
		tab.put("Chypre","Nicosie");
		tab.put("Colombie","Bogota");
		tab.put("Comores","Moroni");
		tab.put("Congo","Brazzaville");
		tab.put("Corée du Nord","Pyongyang");
		tab.put("Corée du Sud","Séoul");
		tab.put("Costa Rica","San José");
		tab.put("Côte d'Ivoire","Yamoussoukro (politique), Abidjan (économique)");
		tab.put("Croatie","Zagreb");
		tab.put("Cuba","La Havane");
		tab.put("Danemark","Copenhague");
		tab.put("Djibouti","Djibouti");
		tab.put("Dominique","Roseau");
		tab.put("Égypte","Le Caire");
		tab.put("Émirats arabes unis","Abou Dabi");
		tab.put("Équateur","Quito");
		tab.put("Érythrée","Asmara");
		tab.put("Espagne","Madrid");
		tab.put("Estonie","Tallinn");
		tab.put("États-Unis","Washington, D.C.");
		tab.put("Éthiopie","Addis-Abeba");
		tab.put("Fidji","Suva");
		tab.put("Finlande","Helsinki");
		tab.put("France","Paris");
		tab.put("Gabon","Libreville");
		tab.put("Gambie","Banjul");
		tab.put("Géorgie","Tbilissi");
		tab.put("Ghana","Accra");
		tab.put("Grèce","Athènes");
		tab.put("Grenade","Saint George's");
		tab.put("Guatemala","Guatemala (chef-lieu), Ciudad de Guatemala (plus grande ville)");
		tab.put("Guinée","Conakry");
		tab.put("Guinée-Bissau","Bissau");
		tab.put("Guinée équatoriale","Malabo");
		tab.put("Guyana","Georgetown");
		tab.put("Haïti","Port-au-Prince");
		tab.put("Honduras","Tegucigalpa");
		tab.put("Hongrie","Budapest");
		tab.put("Îles Marshall","Majuro");
		tab.put("Îles Salomon","Honiara");
		tab.put("Inde","New Delhi");
		tab.put("Indonésie","Jakarta");
		tab.put("Irak","Bagdad");
		tab.put("Iran","Téhéran");
		tab.put("Irlande","Dublin");
		tab.put("Islande","Reykjavik");
		tab.put("Israël","Jérusalem (disputée), Tel Aviv (reconnue internationalement)");
		tab.put("Italie","Rome");
		tab.put("Jamaïque","Kingston");
		tab.put("Japon","Tokyo");
		tab.put("Jordanie","Amman");
		tab.put("Kazakhstan","Noursoultan (Astana jusqu'en 2019)");
		tab.put("Kenya","Nairobi");
		tab.put("Kirghizistan","Bichkek");
		tab.put("Kiribati","Tarawa-Sud");
		tab.put("Koweït","Koweït");
		tab.put("Laos","Vientiane");
		tab.put("Lesotho","Maseru");
		tab.put("Lettonie","Riga");
		tab.put("Liban","Beyrouth");
		tab.put("Liberia","Monrovia");
		tab.put("Libye","Tripoli");
		tab.put("Liechtenstein","Vaduz");
		tab.put("Lituanie","Vilnius");
		tab.put("Luxembourg","Luxembourg");
		tab.put("Macédoine du Nord","Skopje");
		tab.put("Madagascar","Antananarivo");
		tab.put("Malaisie","Kuala Lumpur");
		tab.put("Malawi","Lilongwe");
		tab.put("Maldives","Malé");
		tab.put("Mali","Bamako");
		tab.put("Malte","La Valette");
		tab.put("Maroc","Rabat (administrative), Casablanca (économique)");
		tab.put("Maurice","Port-Louis");
		tab.put("Mauritanie","Nouakchott");
		tab.put("Mexique","Mexico");
		tab.put("Micronésie","Palikir");
		tab.put("Moldavie","Chișinău");
		tab.put("Monaco","Monaco");
		tab.put("Mongolie","Oulan-Bator");
		tab.put("Monténégro","Podgorica");
		tab.put("Mozambique","Maputo");
		tab.put("Namibie","Windhoek");
		tab.put("Nauru","Yaren (législative), Yaren District (exécutif)");
		tab.put("Népal","Katmandou");
		tab.put("Nicaragua","Managua");
		tab.put("Niger","Niamey");
		tab.put("Nigeria","Abuja (politique), Lagos (économique)");
		tab.put("Norvège","Oslo");
		tab.put("Nouvelle-Zélande","Wellington");
		tab.put("Oman","Mascate");
		tab.put("Ouganda","Kampala");
		tab.put("Ouzbékistan","Tachkent");
		tab.put("Pakistan","Islamabad");
		tab.put("Palaos","Ngerulmud");
		tab.put("Palestine","Jérusalem-Est (revendiquée), Ramallah (siège de l'Autorité palestinienne)");
		tab.put("Panama","Panama");
		tab.put("Papouasie-Nouvelle-Guinée","Port Moresby");
		tab.put("Paraguay","Asunción");
		tab.put("Pays-Bas","Amsterdam (capitale constitutionnelle), La Haye (siège du gouvernement)");
		tab.put("Pérou","Lima");
		tab.put("Philippines","Manille");
		tab.put("Pologne","Varsovie");
		tab.put("Portugal","Lisbonne");
		tab.put("Qatar","Doha");
		tab.put("République centrafricaine","Bangui");
		tab.put("République du Congo","Brazzaville");
		tab.put("République dominicaine","Saint-Domingue");
		tab.put("République tchèque","Prague");
		tab.put("Roumanie","Bucarest");
		tab.put("Royaume-Uni","Londres");
		tab.put("Russie","Moscou");
		tab.put("Rwanda","Kigali");
		tab.put("Saint-Christophe-et-Niévès","Basseterre");
		tab.put("Saint-Marin","Saint-Marin");
		tab.put("Saint-Vincent-et-les-Grenadines","Kingstown");
		tab.put("Sainte-Lucie","Castries");
		tab.put("Salvador","San Salvador");
		tab.put("Samoa","Apia");
		tab.put("São Tomé-et-Principe","São Tomé");
		tab.put("Sénégal","Dakar");
		tab.put("Serbie","Belgrade");
		tab.put("Seychelles","Victoria");
		tab.put("Sierra Leone","Freetown");
		tab.put("Singapour","Singapour");
		tab.put("Slovaquie","Bratislava");
		tab.put("Slovénie","Ljubljana");
		tab.put("Somalie","Mogadiscio");
		tab.put("Soudan","Khartoum");
		tab.put("Soudan du Sud","Djouba");
		tab.put("Sri Lanka","Colombo");
		tab.put("Suède","Stockholm");
		tab.put("Suisse","Berne");
		tab.put("Suriname","Paramaribo");
		tab.put("Swaziland","Lobamba (royaume), Mbabane (capitale administrative)");
		tab.put("Syrie","Damas");
		tab.put("Tadjikistan","Douchanbé");
		tab.put("Tanzanie","Dodoma (politique), Dar es Salam (économique)");
		tab.put("Tchad","N'Djaména");
		tab.put("Thaïlande","Bangkok");
		tab.put("Timor-Oriental","Dili");
		tab.put("Togo","Lomé");
		tab.put("Tonga","Nuku'alofa");
		tab.put("Trinité-et-Tobago","Port-d'Espagne");
		tab.put("Tunisie","Tunis");
		tab.put("Turkménistan","Achgabat");
		tab.put("Turquie","Ankara");
		tab.put("Tuvalu","Funafuti");
		tab.put("Ukraine","Kiev");
		tab.put("Uruguay","Montevideo");
		tab.put("Vanuatu","Port-Vila");
		tab.put("Vatican","Vatican");
		tab.put("Venezuela","Caracas");
		tab.put("Viêt Nam","Hanoï");
		tab.put("Yémen","Sanaa");
		tab.put("Zambie","Lusaka");
		tab.put("Zimbabwe","Harare");
	}//FinMethod
	
	private static final String[] paysDuMonde = {
		    "Afghanistan",
		    "Afrique du Sud",
		    "Albanie",
		    "Algérie",
		    "Allemagne",
		    "Andorre",
		    "Angola",
		    "Antigua-et-Barbuda",
		    "Arabie saoudite",
		    "Argentine",
		    "Arménie",
		    "Australie",
		    "Autriche",
		    "Azerbaïdjan",
		    "Bahamas",
		    "Bahreïn",
		    "Bangladesh",
		    "Barbade",
		    "Bélarus (Biélorussie)",
		    "Belgique",
		    "Belize",
		    "Bénin",
		    "Bhoutan",
		    "Birmanie (Myanmar)",
		    "Bolivie",
		    "Bosnie-Herzégovine",
		    "Botswana",
		    "Brésil",
		    "Brunei",
		    "Bulgarie",
		    "Burkina Faso",
		    "Burundi",
		    "Cambodge",
		    "Cameroun",
		    "Canada",
		    "Cap-Vert",
		    "Chili",
		    "Chine",
		    "Chypre",
		    "Colombie",
		    "Comores",
		    "Congo",
		    "Corée du Nord",
		    "Corée du Sud",
		    "Costa Rica",
		    "Côte d'Ivoire",
		    "Croatie",
		    "Cuba",
		    "Danemark",
		    "Djibouti",
		    "Dominique",
		    "Égypte",
		    "Émirats arabes unis",
		    "Équateur",
		    "Érythrée",
		    "Espagne",
		    "Estonie",
		    "États-Unis",
		    "Éthiopie",
		    "Fidji",
		    "Finlande",
		    "France",
		    "Gabon",
		    "Gambie",
		    "Géorgie",
		    "Ghana",
		    "Grèce",
		    "Grenade",
		    "Guatemala",
		    "Guinée",
		    "Guinée-Bissau",
		    "Guinée équatoriale",
		    "Guyana",
		    "Haïti",
		    "Honduras",
		    "Hongrie",
		    "Îles Marshall",
		    "Îles Salomon",
		    "Inde",
		    "Indonésie",
		    "Irak",
		    "Iran",
		    "Irlande",
		    "Islande",
		    "Israël",
		    "Italie",
		    "Jamaïque",
		    "Japon",
		    "Jordanie",
		    "Kazakhstan",
		    "Kenya",
		    "Kirghizistan",
		    "Kiribati",
		    "Koweït",
		    "Laos",
		    "Lesotho",
		    "Lettonie",
		    "Liban",
		    "Liberia",
		    "Libye",
		    "Liechtenstein",
		    "Lituanie",
		    "Luxembourg",
		    "Macédoine du Nord",
		    "Madagascar",
		    "Malaisie",
		    "Malawi",
		    "Maldives",
		    "Mali",
		    "Malte",
		    "Maroc",
		    "Maurice",
		    "Mauritanie",
		    "Mexique",
		    "Micronésie",
		    "Moldavie",
		    "Monaco",
		    "Mongolie",
		    "Monténégro",
		    "Mozambique",
		    "Namibie",
		    "Nauru",
		    "Népal",
		    "Nicaragua",
		    "Niger",
		    "Nigeria",
		    "Norvège",
		    "Nouvelle-Zélande",
		    "Oman",
		    "Ouganda",
		    "Ouzbékistan",
		    "Pakistan",
		    "Palaos",
		    "Palestine",
		    "Panama",
		    "Papouasie-Nouvelle-Guinée",
		    "Paraguay",
		    "Pays-Bas",
		    "Pérou",
		    "Philippines",
		    "Pologne",
		    "Portugal",
		    "Qatar",
		    "République centrafricaine",
		    "République du Congo",
		    "République dominicaine",
		    "République tchèque",
		    "Roumanie",
		    "Royaume-Uni",
		    "Russie",
		    "Rwanda",
		    "Saint-Christophe-et-Niévès",
		    "Saint-Marin",
		    "Saint-Vincent-et-les-Grenadines",
		    "Sainte-Lucie",
		    "Salvador",
		    "Samoa",
		    "São Tomé-et-Principe",
		    "Sénégal",
		    "Serbie",
		    "Seychelles",
		    "Sierra Leone",
		    "Singapour",
		    "Slovaquie",
		    "Slovénie",
		    "Somalie",
		    "Soudan",
		    "Soudan du Sud",
		    "Sri Lanka",
		    "Suède",
		    "Suisse",
		    "Suriname",
		    "Swaziland",
		    "Syrie",
		    "Tadjikistan",
		    "Tanzanie",
		    "Tchad",
		    "Thaïlande",
		    "Timor-Oriental",
		    "Togo",
		    "Tonga",
		    "Trinité-et-Tobago",
		    "Tunisie",
		    "Turkménistan",
		    "Turquie",
		    "Tuvalu",
		    "Ukraine",
		    "Uruguay",
		    "Vanuatu",
		    "Vatican",
		    "Venezuela",
		    "Viêt Nam",
		    "Yémen",
		    "Zambie",
		    "Zimbabwe"
		};
	
	private static int[] tabuList = new int[]{127,97,54,103,77,165,131};
	
	public static boolean isPresent(int u){
		for(int i = 0; i < tabuList.length ;i++) {
			if(tabuList[i] == u) {
				return false;
			}
		}
		return true;
	}//FinMethod
	
	
	public static void main(String[] args) {
		Random r = new  Random();
		initHash();
		int val = -4;
		while(isPresent(val) & val <0) {
			val = r.nextInt(paysDuMonde.length);
		}
		System.out.println(val+" : "+paysDuMonde[val]);
		System.out.println("Capitale : "+tab.get(paysDuMonde[val]));
	}//FinMethod
	//madagascar Tananarive indpéendance de la france 1960 
	//Nouvelle zelande Wellington
	//Liban beyrouth
	//Érythrée Asmara
	//Macédoine du Nord Skopje
	//Iles Salomon Honiara
	//Somalie Modagiscio
	//Pakistan Islamabad
}//FinClass
