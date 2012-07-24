package tatoo;

import tatoo.model.conditions.CalculatedNumber;
import tatoo.model.conditions.CalculatedNumber.Arithmetic;
import tatoo.model.conditions.Condition.ConditionTypes;
import tatoo.model.conditions.NumberCondition;
import tatoo.model.entities.AbstractEntity;
import tatoo.model.entities.ArmyListEntity;

public class ArmyListInstanceSidePanel {

  // zu Testzwecken freigegeben über kurz oder lang muss das hier weg!
  public AbstractEntity armeeliste;

  // private ArmyListEntity andUpgrade(){ return new AnyOfEntity("beliebiges aus"); }
  private AbstractEntity andUpgrade() {
    return new ArmyListEntity(AbstractEntity.EntityType.ANYOFUPGRADE, "beliebiges aus");
  }

  // private ArmyListEntity orUpgrade(){ return new OneOfEntity("eines aus"); }
  private AbstractEntity orUpgrade() {
    return new ArmyListEntity(AbstractEntity.EntityType.ONEOFUPGRADE, "beliebiges aus");
  }

  public ArmyListInstanceSidePanel() {

    //armeeliste = (ArmyListEntity) DBFactory.getInstance().read(ArmyListEntity.class, 3);
    // armeeliste = generateManually();

    if (armeeliste == null) {
      armeeliste = generateManually();
      //DBFactory.getInstance().write(armeeliste);
      // armeeliste = (RootNode)DBFactory.getInstance().read(RootNode.class, 4);
    }

  }

  private AbstractEntity generateManually() {
    AbstractEntity armeeliste = new ArmyListEntity(AbstractEntity.EntityType.ROOT, "Armeeliste");

    /* Entry */
//    AbstractEntity hq = new ArmyListEntity(AbstractEntity.EntityType.CATEGORY, "HQ");
//    armeeliste.addEntity(hq);
//    hq.setAttribute(1, ConditionTypes.MIN_COUNT);
//    hq.setAttribute(2, ConditionTypes.MAX_COUNT);
//
//    AbstractEntity elite = new ArmyListEntity(AbstractEntity.EntityType.CATEGORY, "Elite");
//    armeeliste.addEntity(elite);
//    elite.setAttribute(0, ConditionTypes.MIN_COUNT);
//    elite.setAttribute(3, ConditionTypes.MIN_COUNT);

    AbstractEntity standard = new ArmyListEntity(AbstractEntity.EntityType.CATEGORY, "Standard");
    armeeliste.addEntity(standard);
    standard.setAttribute(2, ConditionTypes.MIN_COUNT);
    standard.setAttribute(6, ConditionTypes.MIN_COUNT);

//    AbstractEntity sturm = new ArmyListEntity(AbstractEntity.EntityType.CATEGORY, "Sturm");
//    armeeliste.addEntity(sturm);
//    sturm.setAttribute(0, ConditionTypes.MIN_COUNT);
//    sturm.setAttribute(3, ConditionTypes.MAX_COUNT);
//
//    AbstractEntity unterstuetzung = new ArmyListEntity(AbstractEntity.EntityType.CATEGORY,
//        "Unterstuetzung");
//    armeeliste.addEntity(unterstuetzung);
//    unterstuetzung.setAttribute(0, ConditionTypes.MIN_COUNT);
//    unterstuetzung.setAttribute(3, ConditionTypes.MAX_COUNT);

    /* Unit */
//    AbstractEntity wb = new ArmyListEntity(AbstractEntity.EntityType.NODE, "Warboss");
//    wb.setAttribute(60, ConditionTypes.PRICE);
//    hq.addEntity(wb);
//
//    AbstractEntity fetterSpalta_wb = new ArmyListEntity(AbstractEntity.EntityType.UPGRADE,
//        "Fetter Spalta");
//    fetterSpalta_wb.setAttribute(0, ConditionTypes.MIN_COUNT);
//    fetterSpalta_wb.setAttribute(1, ConditionTypes.MAX_COUNT);
//    fetterSpalta_wb.setAttribute(5, ConditionTypes.PRICE);
//    AbstractEntity energieKlaue_wb = new ArmyListEntity(AbstractEntity.EntityType.UPGRADE,
//        "Energiekrallä");
//    energieKlaue_wb.setAttribute(0, ConditionTypes.MIN_COUNT);
//    energieKlaue_wb.setAttribute(1, ConditionTypes.MAX_COUNT);
//    energieKlaue_wb.setAttribute(25, ConditionTypes.PRICE);
//    AbstractEntity orUpgrade2_wb = orUpgrade();
//    orUpgrade2_wb.addEntity(fetterSpalta_wb);
//    orUpgrade2_wb.addEntity(energieKlaue_wb);
//    AbstractEntity Bazz_Kombi_wb = new ArmyListEntity(AbstractEntity.EntityType.UPGRADE,
//        "Bazzuka-Kombi");
//    Bazz_Kombi_wb.setAttribute(0, ConditionTypes.MIN_COUNT);
//    Bazz_Kombi_wb.setAttribute(1, ConditionTypes.MAX_COUNT);
//    Bazz_Kombi_wb.setAttribute(5, ConditionTypes.PRICE);
//    AbstractEntity Gitbrenna_Kombi_wb = new ArmyListEntity(AbstractEntity.EntityType.UPGRADE,
//        "Gitbrenna-Kombi");
//    Gitbrenna_Kombi_wb.setAttribute(0, ConditionTypes.MIN_COUNT);
//    Gitbrenna_Kombi_wb.setAttribute(1, ConditionTypes.MAX_COUNT);
//    Gitbrenna_Kombi_wb.setAttribute(5, ConditionTypes.PRICE);
//    AbstractEntity SyncWumme_wb = new ArmyListEntity(AbstractEntity.EntityType.UPGRADE,
//        "Sync. Wumme");
//    SyncWumme_wb.setAttribute(0, ConditionTypes.MIN_COUNT);
//    SyncWumme_wb.setAttribute(1, ConditionTypes.MAX_COUNT);
//    SyncWumme_wb.setAttribute(5, ConditionTypes.PRICE);
//    AbstractEntity orUpgrade3_wb = orUpgrade();
//    orUpgrade3_wb.addEntity(Bazz_Kombi_wb);
//    orUpgrade3_wb.addEntity(Gitbrenna_Kombi_wb);
//    orUpgrade3_wb.addEntity(SyncWumme_wb);
//    AbstractEntity Munigrot_wb = new ArmyListEntity(AbstractEntity.EntityType.UPGRADE,
//        "Munigrot");
//    Munigrot_wb.setAttribute(0, ConditionTypes.MIN_COUNT);
//    Munigrot_wb.setAttribute(1, ConditionTypes.MAX_COUNT);
//    Munigrot_wb.setAttribute(3, ConditionTypes.PRICE);
//    AbstractEntity Squik_wb = new ArmyListEntity(AbstractEntity.EntityType.UPGRADE,
//        "Schnappasquik");
//    Squik_wb.setAttribute(0, ConditionTypes.MIN_COUNT);
//    Squik_wb.setAttribute(1, ConditionTypes.MAX_COUNT);
//    Squik_wb.setAttribute(15, ConditionTypes.PRICE);
//    AbstractEntity CyborgKoerpa_wb = new ArmyListEntity(AbstractEntity.EntityType.UPGRADE,
//        "Cyborg Körpa");
//    CyborgKoerpa_wb.setAttribute(0, ConditionTypes.MIN_COUNT);
//    CyborgKoerpa_wb.setAttribute(1, ConditionTypes.MAX_COUNT);
//    CyborgKoerpa_wb.setAttribute(10, ConditionTypes.PRICE);
//    AbstractEntity stange_wb = new ArmyListEntity(AbstractEntity.EntityType.UPGRADE,
//        "Trophäenstangä");
//    stange_wb.setAttribute(0, ConditionTypes.MIN_COUNT);
//    stange_wb.setAttribute(1, ConditionTypes.MAX_COUNT);
//    stange_wb.setAttribute(5, ConditionTypes.PRICE);
//    AbstractEntity Panzaruestung_wb = new ArmyListEntity(AbstractEntity.EntityType.UPGRADE,
//        "Panzarüstung");
//    Panzaruestung_wb.setAttribute(0, ConditionTypes.MIN_COUNT);
//    Panzaruestung_wb.setAttribute(1, ConditionTypes.MAX_COUNT);
//    Panzaruestung_wb.setAttribute(5, ConditionTypes.PRICE);
//    AbstractEntity andUpgrade3 = andUpgrade();
//    andUpgrade3.addEntity(Munigrot_wb);
//    andUpgrade3.addEntity(Squik_wb);
//    andUpgrade3.addEntity(CyborgKoerpa_wb);
//    andUpgrade3.addEntity(stange_wb);
//    andUpgrade3.addEntity(Panzaruestung_wb);
//    AbstractEntity andUpgrade4 = andUpgrade();
//    andUpgrade4.addEntity(orUpgrade2_wb);
//    andUpgrade4.addEntity(orUpgrade3_wb);
//    andUpgrade4.addEntity(andUpgrade3);
//
//    wb.addEntity(andUpgrade4);
//
//    AbstractEntity mek = new ArmyListEntity(AbstractEntity.EntityType.NODE, "Bigmek");
//    mek.setAttribute(110, ConditionTypes.PRICE);
//    hq.addEntity(mek);
//
//    AbstractEntity fetterSpalta = new ArmyListEntity(AbstractEntity.EntityType.UPGRADE,
//        "Fetter Spalta");
//    fetterSpalta.setAttribute(0, ConditionTypes.MIN_COUNT);
//    fetterSpalta.setAttribute(1, ConditionTypes.MAX_COUNT);
//    fetterSpalta.setAttribute(5, ConditionTypes.PRICE);
//    AbstractEntity energieKlaue = new ArmyListEntity(AbstractEntity.EntityType.UPGRADE,
//        "Energiekrallä");
//    energieKlaue.setAttribute(0, ConditionTypes.MIN_COUNT);
//    energieKlaue.setAttribute(1, ConditionTypes.MAX_COUNT);
//    energieKlaue.setAttribute(25, ConditionTypes.PRICE);
//    AbstractEntity stange = new ArmyListEntity(AbstractEntity.EntityType.UPGRADE,
//        "Trophänstangä");
//    stange.setAttribute(0, ConditionTypes.MIN_COUNT);
//    stange.setAttribute(1, ConditionTypes.MAX_COUNT);
//    stange.setAttribute(5, ConditionTypes.PRICE);
//    AbstractEntity panza = new ArmyListEntity(AbstractEntity.EntityType.UPGRADE, "Panzarüstung");
//    panza.setAttribute(0, ConditionTypes.MIN_COUNT);
//    panza.setAttribute(1, ConditionTypes.MAX_COUNT);
//    panza.setAttribute(5, ConditionTypes.PRICE);
//    AbstractEntity orUpgrade2 = orUpgrade();
//    orUpgrade2.addEntity(fetterSpalta);
//    orUpgrade2.addEntity(energieKlaue);
//    AbstractEntity andUpgrade2 = andUpgrade();
//    andUpgrade2.addEntity(orUpgrade2);
//    andUpgrade2.addEntity(stange);
//    andUpgrade2.addEntity(panza);
//    AbstractEntity boyzBoss = new ArmyListEntity(AbstractEntity.EntityType.UPGRADE, "Boss");
//    boyzBoss.setAttribute(0, ConditionTypes.MIN_COUNT);
//    boyzBoss.setAttribute(1, ConditionTypes.MAX_COUNT);
//    boyzBoss.setAttribute(10, ConditionTypes.PRICE);
//    boyzBoss.addEntity(andUpgrade2);
    AbstractEntity boy = new ArmyListEntity(AbstractEntity.EntityType.UPGRADE, "Boy");
    boy.setAttribute(10, ConditionTypes.MIN_COUNT);
    boy.setAttribute(30, ConditionTypes.MAX_COUNT);
    boy.setAttribute(10, ConditionTypes.COUNT);
    boy.setAttribute(6, ConditionTypes.PRICE);
//    boy.addEntity(boyzBoss);
    AbstractEntity stikkbombz = new ArmyListEntity(AbstractEntity.EntityType.UPGRADE,
        "Stikkbombz");
    stikkbombz.setAttribute(0, ConditionTypes.MIN_COUNT);
//    stikkbombz.setAttribute(1, ConditionTypes.MAX_COUNT);

    @SuppressWarnings("unchecked")
    CalculatedNumber stikkbombzCount = new CalculatedNumber(
        (NumberCondition<Integer>) boy.getAttribute(ConditionTypes.COUNT), 1,
        Arithmetic.MULTIPLY);
    stikkbombz.setAttribute(1, ConditionTypes.PRICE);
    stikkbombz.setAttribute(stikkbombzCount, ConditionTypes.MAX_COUNT);

    stikkbombzCount.addChangeListener(stikkbombz);

//    AbstractEntity wummen = new ArmyListEntity(AbstractEntity.EntityType.UPGRADE, "Wummen");
//    wummen.setAttribute(0, ConditionTypes.MIN_COUNT);
//    wummen.setAttribute(1, ConditionTypes.MAX_COUNT);
//    wummen.setAttribute(0, ConditionTypes.PRICE);
    AbstractEntity fetteWumme = new ArmyListEntity(AbstractEntity.EntityType.UPGRADE,
        "Fette Wumme");
    AbstractEntity bazzukka = new ArmyListEntity(AbstractEntity.EntityType.UPGRADE, "Bazzukka");
    fetteWumme.setAttribute(0, ConditionTypes.MIN_COUNT);
    @SuppressWarnings({ "unchecked", "rawtypes" })
    CalculatedNumber fwMax = new CalculatedNumber(
          new CalculatedNumber( (NumberCondition) boy.getAttribute(ConditionTypes.COUNT), 10, Arithmetic.DIVIDE),
          (NumberCondition) bazzukka.getAttribute(ConditionTypes.COUNT), Arithmetic.SUBTRACT
        );
    fwMax.addChangeListener(fetteWumme);
    fetteWumme.setAttribute(fwMax, ConditionTypes.MAX_COUNT);
    fetteWumme.setAttribute(5, ConditionTypes.PRICE);

    bazzukka.setAttribute(0, ConditionTypes.MIN_COUNT);
    @SuppressWarnings({ "unchecked", "rawtypes" })
    CalculatedNumber bazMax = new CalculatedNumber(new CalculatedNumber(
        (NumberCondition) boy.getAttribute(ConditionTypes.COUNT), 10,
        Arithmetic.DIVIDE),
        (NumberCondition) fetteWumme.getAttribute(ConditionTypes.COUNT),
        Arithmetic.SUBTRACT);
    bazMax.addChangeListener(bazzukka);
    bazzukka.setAttribute(bazMax, ConditionTypes.MAX_COUNT);
    bazzukka.setAttribute(10, ConditionTypes.PRICE);
    AbstractEntity orUpgrade1 = orUpgrade();
    orUpgrade1.addEntity(fetteWumme);
    orUpgrade1.addEntity(bazzukka);
    AbstractEntity andUpgrade1 = andUpgrade();
    andUpgrade1.addEntity(stikkbombz);
//    andUpgrade1.addEntity(wummen);
    andUpgrade1.addEntity(orUpgrade1);
    AbstractEntity orUpgrade_boyz = orUpgrade();
    orUpgrade_boyz.addEntity(boy);
    orUpgrade_boyz.addEntity(andUpgrade1);
    AbstractEntity boyz = new ArmyListEntity(AbstractEntity.EntityType.NODE, "Orkboyz");
    boyz.setAttribute(0, ConditionTypes.MIN_COUNT);
    boyz.setAttribute(0, ConditionTypes.MAX_COUNT);
    boyz.addEntity(orUpgrade_boyz);
    standard.addEntity(boyz);

//    AbstractEntity bosse = new ArmyListEntity(AbstractEntity.EntityType.NODE, "Bosse");
//    bosse.setAttribute(0, ConditionTypes.MIN_COUNT);
//    bosse.setAttribute(0, ConditionTypes.MAX_COUNT);
//    elite.addEntity(bosse);
//    AbstractEntity boss = new ArmyListEntity(AbstractEntity.EntityType.UPGRADE, "Boss");
//    boss.setAttribute(20, ConditionTypes.PRICE);
//    bosse.addEntity(boss);

    return armeeliste;

  }

}
