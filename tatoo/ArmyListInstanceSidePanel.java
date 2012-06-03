package tatoo;

import tatoo.db.DBFactory;
import tatoo.model.conditions.CalculatedNumber;
import tatoo.model.conditions.NumberCondition;
import tatoo.model.conditions.CalculatedNumber.Arithmetic;
import tatoo.model.entities.AnyOfUpgradeNode;
import tatoo.model.entities.CategoryNode;
import tatoo.model.entities.Entity;
import tatoo.model.entities.NodeNode;
import tatoo.model.entities.OneOfUpgradeNode;
import tatoo.model.entities.RootNode;
import tatoo.model.entities.UpgradeNode;

public class ArmyListInstanceSidePanel {
	
  //zu Testzwecken freigegeben über kurz oder lang muss das hier weg!
	public Entity armeeliste;
	
	private Entity andUpgrade(){ return new AnyOfUpgradeNode("beliebiges aus"); }
	private Entity orUpgrade(){ return new OneOfUpgradeNode("eines aus"); }
	
	public ArmyListInstanceSidePanel(){
			
			armeeliste = new RootNode("Armeeliste");
			//armeeliste.save();
//			DBFactory.getInstance().getConnection().save(armeeliste);

//			/* Entry */
//			Entity hq = new CategoryNode("HQ");
//			armeeliste.addEntity(hq);
//			hq.setMinCount(1);
//			hq.setMaxCount(2);
//
//			Entity elite = new CategoryNode("Elite");
//			armeeliste.addEntity(elite);
//			elite.setMinCount(0);
//			elite.setMaxCount(3);
//
//			Entity standard = new CategoryNode("Standard");
//			armeeliste.addEntity(standard);
//			standard.setMinCount(2);
//			standard.setMaxCount(6);
//
//			Entity sturm = new CategoryNode("Sturm");
//			armeeliste.addEntity(sturm);
//			sturm.setMinCount(0);
//			sturm.setMaxCount(3);
//
//			Entity unterstuetzung = new CategoryNode("Unterstuetzung");
//			armeeliste.addEntity(unterstuetzung);
//			unterstuetzung.setMinCount(0);
//			unterstuetzung.setMaxCount(3);
//
//			/* Unit */
//			Entity wb = new NodeNode("Warboss");
//			wb.setPrice(60);
//			hq.addEntity(wb);
//			
//			Entity fetterSpalta_wb = new UpgradeNode("Fetter Spalta");
//			fetterSpalta_wb.setMinCount(0);
//			fetterSpalta_wb.setMaxCount(1);
//			fetterSpalta_wb.setPrice(5);
//	    Entity energieKlaue_wb = new UpgradeNode("Energiekrallä");
//	    energieKlaue_wb.setMinCount(0);
//	    energieKlaue_wb.setMaxCount(1);
//	    energieKlaue_wb.setPrice(25);
//	    Entity orUpgrade2_wb = orUpgrade();
//	    orUpgrade2_wb.addEntity(fetterSpalta_wb);
//	    orUpgrade2_wb.addEntity(energieKlaue_wb);
//	    Entity Bazz_Kombi_wb = new UpgradeNode("Bazzuka-Kombi");
//	    Bazz_Kombi_wb.setMinCount(0);
//	    Bazz_Kombi_wb.setMaxCount(1);
//	    Bazz_Kombi_wb.setPrice(5);
//	    Entity Gitbrenna_Kombi_wb = new UpgradeNode("Gitbrenna-Kombi");
//	    Gitbrenna_Kombi_wb.setMinCount(0);
//	    Gitbrenna_Kombi_wb.setMaxCount(1);
//	    Gitbrenna_Kombi_wb.setPrice(5);
//	    Entity SyncWumme_wb = new UpgradeNode("Sync. Wumme");
//	    SyncWumme_wb.setMinCount(0);
//	    SyncWumme_wb.setMaxCount(1);
//	    SyncWumme_wb.setPrice(5);
//	    Entity orUpgrade3_wb = orUpgrade();
//	    orUpgrade3_wb.addEntity(Bazz_Kombi_wb);
//	    orUpgrade3_wb.addEntity(Gitbrenna_Kombi_wb);
//	    orUpgrade3_wb.addEntity(SyncWumme_wb);
//	    Entity Munigrot_wb = new UpgradeNode("Munigrot");
//	    Munigrot_wb.setMinCount(0);
//	    Munigrot_wb.setMaxCount(1);
//	    Munigrot_wb.setPrice(3);
//	    Entity Squik_wb = new UpgradeNode("Schnappasquik");
//	    Squik_wb.setMinCount(0);
//	    Squik_wb.setMaxCount(1);
//	    Squik_wb.setPrice(15);
//	    Entity CyborgKoerpa_wb = new UpgradeNode("Cyborg Körpa");
//	    CyborgKoerpa_wb.setMinCount(0);
//	    CyborgKoerpa_wb.setMaxCount(1);
//	    CyborgKoerpa_wb.setPrice(10);
//	    Entity stange_wb = new UpgradeNode("Trophä'enstangä");
//	    stange_wb.setMinCount(0);
//	    stange_wb.setMaxCount(1);
//	    stange_wb.setPrice(5);
//	    Entity Panzaruestung_wb = new UpgradeNode("Panzarüstung");
//	    Panzaruestung_wb.setMinCount(0);
//	    Panzaruestung_wb.setMaxCount(1);
//	    Panzaruestung_wb.setPrice(5);
//	    Entity andUpgrade3 = andUpgrade();
//	    andUpgrade3.addEntity(Munigrot_wb);
//	    andUpgrade3.addEntity(Squik_wb);
//	    andUpgrade3.addEntity(CyborgKoerpa_wb);
//	    andUpgrade3.addEntity(stange_wb);
//	    andUpgrade3.addEntity(Panzaruestung_wb);
//	    Entity andUpgrade4 = andUpgrade();
//	    andUpgrade4.addEntity(orUpgrade2_wb);
//	    andUpgrade4.addEntity(orUpgrade3_wb);
//	    andUpgrade4.addEntity(andUpgrade3);
//	    
//	    wb.addEntity(andUpgrade4);
//
//			Entity mek = new NodeNode("Bigmek");
//			mek.setPrice(110);
//			hq.addEntity(mek);
//
//			Entity fetterSpalta = new UpgradeNode("Fetter Spalta");
//			fetterSpalta.setMinCount(0);
//			fetterSpalta.setMaxCount(1);
//			fetterSpalta.setPrice(5);
//	    Entity energieKlaue = new UpgradeNode("Energiekrallä");
//	    energieKlaue.setMinCount(0);
//	    energieKlaue.setMaxCount(1);
//	    energieKlaue.setPrice(25);
//	    Entity stange = new UpgradeNode("Trophä'nstangä");
//	    stange.setMinCount(0);
//	    stange.setMaxCount(1);
//	    stange.setPrice(5);
//	    Entity panza = new UpgradeNode("Panzarüstung");
//	    panza.setMinCount(0);
//	    panza.setMaxCount(1);
//	    panza.setPrice(5);
//	    Entity orUpgrade2 = orUpgrade();
//	    orUpgrade2.addEntity(fetterSpalta);
//	    orUpgrade2.addEntity(energieKlaue);
//	    Entity andUpgrade2 = andUpgrade();
//	    andUpgrade2.addEntity(orUpgrade2);
//	    andUpgrade2.addEntity(stange);
//	    andUpgrade2.addEntity(panza);
//	    Entity boyzBoss = new UpgradeNode("Boss");
//	    boyzBoss.setMinCount(0);
//	    boyzBoss.setMaxCount(1);
//	    boyzBoss.setPrice(10);
//	    boyzBoss.addEntity(andUpgrade2);
//	    Entity boy = new UpgradeNode("Boy");
//			boy.setMinCount(10);
//			boy.setMaxCount(30);
//			boy.setPrice(6);
//			boy.addEntity(boyzBoss);
//	    Entity stikkbombz = new UpgradeNode("Stikkbombz");
//	    stikkbombz.setMinCount(0);
//	    stikkbombz.setMaxCount(1);
//	    //anfang
//	    CalculatedNumber stikkbombzCount = new CalculatedNumber((NumberCondition)boy.getCount(),1, Arithmetic.MULTIPLY);
//	    //CalculatedNumber stikkbombzPrice = new CalculatedNumber((NumberCondition)boy.getCount(),1,Arithmetic.MULTIPLY);
//	    //stikkbombz.setPrice(stikkbombzPrice);
//	    //stikkbombzPrice.addChangeListener(stikkbombz);
//	    stikkbombz.setPrice(1);
//	    stikkbombz.setMaxCount(stikkbombzCount);
//	    stikkbombzCount.addChangeListener(stikkbombz);
//	    //ende
//	    Entity wummen = new UpgradeNode("Wummen");
//	    wummen.setMinCount(0);
//	    wummen.setMaxCount(1);
//	    wummen.setPrice(0);
//	    Entity fetteWumme = new UpgradeNode("Fette Wumme");
//	    Entity bazzukka = new UpgradeNode("Bazzukka");
//	    fetteWumme.setMinCount(0);
//	    CalculatedNumber fwMax = 
//	    	new CalculatedNumber(
//	    			new CalculatedNumber(boy.getCountAsCondition(), 10, Arithmetic.DIVIDE),
//	    			bazzukka.getCountAsCondition(), 
//	    			Arithmetic.SUBTRACT
//	    	);
//	    fwMax.addChangeListener(fetteWumme);
//	    fetteWumme.setMaxCount(fwMax);
//	    fetteWumme.setPrice(5);
//	    
//	    bazzukka.setMinCount(0);
//	    CalculatedNumber bazMax = 
//	    	new CalculatedNumber(
//	    			new CalculatedNumber(boy.getCountAsCondition(), 10, Arithmetic.DIVIDE ),
//	    			fetteWumme.getCountAsCondition(),
//	    			Arithmetic.SUBTRACT
//	    	);
//	    bazMax.addChangeListener(bazzukka);
//	    bazzukka.setMaxCount(bazMax);
//	    bazzukka.setPrice(10);
//	    Entity orUpgrade1 = orUpgrade();
//	    orUpgrade1.addEntity(fetteWumme);
//	    orUpgrade1.addEntity(bazzukka);
//	    Entity andUpgrade1 = andUpgrade();
//	    andUpgrade1.addEntity(stikkbombz);
//	    andUpgrade1.addEntity(wummen);
//	    andUpgrade1.addEntity(orUpgrade1);
//			Entity orUpgrade_boyz = orUpgrade();
//			orUpgrade_boyz.addEntity(boy);
//			orUpgrade_boyz.addEntity(andUpgrade1);
//			Entity boyz = new NodeNode("Orkboyz");
//	    boyz.setMinCount(0);
//	    boyz.setMaxCount(0);
//	    boyz.addEntity(orUpgrade_boyz);
//	    standard.addEntity(boyz);
//	    
//			
//			Entity bosse = new NodeNode("Bosse");
//			bosse.setMinCount(0);
//			bosse.setMaxCount(0);
//			elite.addEntity(bosse);
//			Entity boss = new UpgradeNode("Boss");
//			boss.setPrice(20);
//			bosse.addEntity(boss);
		
	}

}
