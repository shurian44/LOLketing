package com.ezen.lolketing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ezen.lolketing.adapter.SliderAdapter
import com.google.firebase.auth.FirebaseAuth
import com.smarteist.autoimageslider.IndicatorAnimations
import com.smarteist.autoimageslider.SliderAnimations
import kotlinx.android.synthetic.main.activity_lol_guide_detail.*

class LoLGuideDetailActivity : AppCompatActivity() {

    private var auth = FirebaseAuth.getInstance()
    lateinit var status : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lol_guide_detail)

        status = intent.getStringExtra("status")

        when(status){
            "aos"->{
                guide_title.setImageResource(R.drawable.lol_guide_aos)
                txt_content.text = "우선 롤이라는 게임의 장르부터 파악해 보겠습니다. 롤의 장르는 ‘AOS(Aeon of Strife)’에 속합니다. 이는 가상 세계에서 하나 이상의 캐릭터를 꾸준하게 육성해 다양한 미션을 수행하는 RPG 게임(리니지, 메이플 스토리 등)과, 특정된 전장 안에서 수많은 캐릭터를 전술적으로 운영해 적을 제압하는 RTS 게임(스타크레프트 등) 두 장르를 반씩 섞어 놓은 새로운 장르로 이해할 수 있습니다.\n\n즉, AOS란 ‘특정한 전장에서 하나의 캐릭터를 육성해 팀원들과 함께 적을 제압하는 게임’으로 규정할 수 있습니다. 플레이어들은 게임에 앞서 자신이 조종할 챔피언(캐릭터)을 선택합니다. 그리고 게임이 시작되면 자신이 선택한 챔피언으로 승리를 위해 적 플레이어들의 챔피언들과 맞서 싸워나갑니다. 롤은 일반적으로 ‘소환사의 협곡’이라는 맵에서 5:5 경기가 진행되기 때문에 각 팀원 간 치열한 전략 싸움이 벌어지곤 합니다."
            }
            "rule"->{
                guide_title.setImageResource(R.drawable.lol_guide_rule)
                txt_content.text = "1.적 챔피언 제압\n플레이어가 게임 내에서 조작할 수 있는 캐릭터를 칭하는 말이 바로 ‘챔피언’입니다. 게임 방송을 보다 보면 종종 ‘이 선수는 챔프 폭이 넓다’라는 말이 나오는데요. 이는 ‘능숙하게 조작할 수 있는 챔피언의 수가 많다’는 뜻과 같습니다. 한 팀은 다섯 개의 챔피언으로 구성되며 기본적으로 각 플레이어는 자신의 챔피언을 조작해 적 챔피언을 제압하는 것을 목표로 합니다.\n\n적 챔피언을 제압하면 일정량의 골드를 획득하게 되고, 이 골드를 사용해 무기나 방어구를 구입할 수 있습니다. 또한, 그렇게 얻은 무기와 방어구를 토대로 다음 전투에서도 상대적 우위를 점할 수 있는데요. 따라서 플레이어들은 적 챔피언 처치를 일차적인 목표로 설정하고 게임을 이끌어 나갑니다."+
                        "\n\n2.적 건물 파괴\n롤은 궁극적으로 ‘넥서스’라는 핵심 건물을 파괴해 승리를 거두는 게임입니다. 그리고 넥서스를 파괴하기 위해서는 ‘포탑(타워)’과 ‘억제기’라는 건물을 먼저 파괴해야 한다는 조건이 붙습니다. 플레이어들은 챔피언을 활용해 1차 포탑 → 2차 포탑 → 억제기포탑 → 억제기 → 쌍둥이 포탑 → 넥서스 순으로 건물을 파괴해야 하며, 적 챔피언이 아군의 건물을 파괴하려 하면 아군의 챔피언으로 그것을 방어해야 합니다. 물론 적군의 건물을 파괴하려 하면 적 챔피언들이 이를 방해하겠지요.\n\n게임 시작과 동시에 아군의 넥서스에서는 끊임없이 ‘미니언’이라는 몬스터가 생성돼 적진을 향해 달려갑니다. 그리고 이 미니언들은 포탑을 파괴하는 데에 아주 중요한 임무를 수행합니다. 포탑은 미니언 → 챔피언 순으로 공격 대상을 인식하기 때문에, 미니언이 포탑의 공격을 대신 맞아 주는 동안 챔피언을 활용해 포탑을 파괴합니다. 또한, 적의 ‘억제기’를 파괴하면 아군 미니언 공격력과 체력이 대폭 상승해 게임을 이끌어 나가기가 한층 수월해집니다. 미니언들을 얼마나 효율적으로 활용하는지에 따라 승패가 갈리기도 하지요."
            }
            "position"->{
                guide_title.setImageResource(R.drawable.lol_guide_position)
                txt_content.text = "가장 일반적으로 사용하는 맵(전장)이자 프로 대회 공식 맵인 ‘소환사의 협곡’에는 총 세 개의 라인이 존재합니다. 경기 초반 각 챔피언은 하나의 라인을 담당해 포탑을 지키는 임무를 수행합니다. 그리고 해당 라인을 담당하는 적팀 챔피언과 지속해서 전투를 벌이며 적을 제압할 기회를 노리기도 합니다. 잠깐, 롤은 5:5 경기라고 했는데 라인이 세 개밖에 없는 것이 이상하다고요? 아래 설명을 보시면 이해가 될 겁니다.\n" +
                        "\n" +
                        "1. 탑 라인: 소환사의 협곡 11시 방향을 따라 뻗어 있는 길을 의미합니다. 이곳을 담당하는 챔피언을 ‘탑 라이너’라고 부릅니다. 세 라인 중 가장 적게 전투가 발생하는 지역입니다. SK텔레콤 T1 팀에서는 ‘후니’ 선수와 ‘운타라’ 선수가 활약하고 있는 라인이지요. 일반적으로 게임 초반에는 미미한 존재감을 보이지만, 게임이 후반부로 향해갈수록 전투에서 탑라이너의 영향력은 무시무시해집니다.\n" +
                        "\n" +
                        "2. 미드 라인: 소환사의 협곡 중심을 향해 뻗어 있는 길을 의미합니다. 이곳을 담당하는 챔피언을 ‘미드 라이너’라고 부릅니다. 순간적으로 적을 제압하는 전투가 자주 발생하는 지역입니다. 팬들 사이에서 ‘세체미(세계 최고 미드 라이너를 뜻하는 약어)’로 불리는 SK텔레콤 T1의 페이커 선수가 담당하고 있는 라인이 바로 이 미드 라인입니다. 미드 라인은 주로 공격력이 강하고 방어력이 약한 챔피언들이 담당하기 때문에 순간적인 판단과 기지에 따라 적을 제압하느냐, 적에게 제압당하느냐가 갈리는 곳입니다.\n" +
                        "\n" +
                        "3. 바텀(약칭 봇) 라인: 세 라인 중 유일하게 두 명의 챔피언이 담당하는 지역입니다. 이곳을 담당하는 챔피언은 ‘원딜러(약칭 원딜)’와 ‘서포터(약칭 서폿)’로 불립니다. 적팀의 챔피언까지 모두 4명의 챔피언이 존재하기 때문에 치열한 전투가 꾸준하게 발생하는 곳이지요. 미드 라이너나 탑 라이너까지 이곳에 합류해 자주 전투를 벌이기도 합니다.\n" +
                        "\n" +
                        "4. 정글: 탑, 미드, 바텀 라인을 제외한 맵의 중앙 부분을 통틀어 ‘정글’이라고 칭합니다. 네 명의 챔피언들이 각 라인을 담당하면 남은 한 명의 챔피언이 바로 이 정글 지역 전체를 담당합니다. 그리고 정글 지역을 담당하는 챔피언은 ‘정글러’로 불립니다. 정글 지역에는 처치 시 골드와 경험치를 제공하는 중립 몬스터들이 존재하며, 정글러들은 이 몬스터들을 처치하며 성장합니다.\n" +
                        "\n" +
                        "각 정글러들은 종종 기회를 노려 각 라인의 전투에 가세하기도 합니다. 이렇게 정글러가 라인 전투에 가세하는 행위를 일컬어 ‘갱킹’이라고 부릅니다. 참고로 롤은 실력이 엇비슷한 유저들끼리 게임 매칭이 되도록 시스템화돼 있습니다. 따라서 미세한 차이로 아슬아슬하게 전투를 벌이고 있는 각 라인에 정글러가 개입하는 순간, 전투에서 승리할 확률이 대폭 상승합니다. 얼마나 적시에 기습적인 갱킹을 성공시키는가가 정글러의 역량을 가늠하는 척도로 작용합니다."
            }
            "nature"->{
                guide_title.setImageResource(R.drawable.lol_guide_nature)
                txt_content.text = "롤 챔피언의 종류는 114개나 되는 관계로 이들을 정확하게 분류하는 것은 사실상 불가능하지만, 포괄적인 역할 군에 따라 크게 네 가지 유형으로 분류하는 것이 보통입니다.\n" +
                        "\n" +
                        "1. 탱커: 공격력에 비해 방어력/체력이 압도적으로 높은 챔피언들을 ‘탱커’로 분류합니다. 이들은 든든한 지구력과 체력을 바탕으로 아군 챔피언들을 향한 공격을 대신 감내하는 임무를 수행합니다. 이들이 전장의 앞쪽에서 적의 공격을 버텨내는 동안, 아군의 공격 담당 챔피언이 적을 제압하는 방식으로 전투가 이뤄집니다. 탑 라인에 자주 등장합니다.\n" +
                        "\n" +
                        "2. 원거리 딜러: 롤의 공격은 ‘물리 공격(AD)’과 ‘마법 공격(AP)’으로 나뉩니다. 원거리 딜러는 전장의 후방에서 지속적으로 적에게 물리 공격을 퍼붓는 임무를 수행합니다. 이름 그대로 먼 거리에서 딜링(적에게 피해를 주는 행위)을 하는 챔피언들을 원거리 딜러로 분류하며, 체력과 방어력이 약합니다. 후반 전투에서는 가장 중요한 영향력을 발휘합니다.\n" +
                        "\n" +
                        "3. 마법 딜러: 물리 공격력은 약하지만 강력한 마법 공격력을 지닌 챔피언들을 마법 딜러로 분류합니다. 마법 사용에는 쿨타임(한 번 사용 후 재사용이 가능하기까지 필요한 시간)이 존재하기 때문에 지속적인 전투에는 취약하지만, 짧은 전투에는 누구보다 무시무시한 화력을 자랑합니다. 주로 미드라인을 담당하는 챔피언들이 마법 딜러입니다.\n" +
                        "\n" +
                        "4. 서포터: 아군의 전투를 보조해 주는 마법을 지닌 챔피언들이 서포터로 분류됩니다. 보호막, 체력 회복, 공격력 향상, 속도 향상 등 아군의 전투 능력을 극대화하는 마법들을 지니고 있습니다. 또한, 적의 움직임을 둔화시키거나 짧은 시간 기절시키는 마법을 한 개 이상 지니고 있습니다."
            }
            "score"->{
                guide_title.setImageResource(R.drawable.lol_guide_score)
                txt_content.text = "롤에 관한 대화나 대회 중계방송을 듣다 보면 자주 언급되는 용어가 있습니다. 그 중 가장 대표적인 것들을 꼽자면 ‘킬/어시’, ‘KDA’, ‘CS’인데요. 이 용어들은 어떤 팀이 어떤 득점을 얼마나 했는지를 가늠할 수 있게 해주는 일종의 점수를 의미합니다. 이 용어들만 알아도 현재 어느 팀이 유리한지 한눈에 파악할 수 있습니다.\n" +
                        "\n" +
                        "1. 킬, 데스, 어시스트: 각각 ‘적 챔피언을 제압한 횟수’, ‘적 챔피언에게 제압당한 횟수’, ‘아군이 적 챔피언을 제압할 때 관여한 횟수’를 의미합니다. 당연히 킬과 어시스트가 높은 팀일수록, 데스가 낮은 팀일수록 경기를 유리하게 이끌어 나가고 있는 것입니다.\n" +
                        "\n" +
                        "2. KDA: 킬 수와 어시스트 수의 합을 데스의 수로 나눈 수치를 의미합니다. 게임 시스템상으로는 노출되지 않는 수치이지만, 각 챔피언의 활약도를 파악하는 용도로 유저들이 생성해 낸 개념입니다. 공식적인 기준은 없지만, 일반적으로 챔피언의 KDA 수치가 3 이상이면 해당 챔피언은 게임 내에서 충분한 활약을 하고 있다고 여깁니다.\n" +
                        "\n" +
                        "3. CS: ‘롤의 꽃’이라고 말해도 무방할 정도로 중요한 수치입니다. 적팀 넥서스에서 생성되는 미니언들을 처치한 횟수를 뜻하는 수치입니다. CS 수치가 높을수록 적 미니언을 많이 처치한 것이고, 그만큼 많은 골드를 벌어들였다는 뜻입니다. 전술했듯이, 높은 골드는 좋은 아이템을 의미하며 이는 곧 다음 전투에서의 상대적 우위를 내포하고 있으므로 높은 CS 수치를 기록하고 있는 챔피언은 아주 강력할 확률이 높습니다.\n" +
                        "\n" +
                        "이제 리그 오브 레전드라는 게임에 대해 조금은 이해가 가시나요? 위의 개념들만 숙지하셔도 외계어처럼 들렸던 롤에 관한 대화들이 귀에 쏙쏙 들어올 겁니다. 다음 글에서는 더욱 자세한 롤 용어들을 알아보는 시간을 갖도록 하겠습니다."
            }
            "terms"->{
                guide_title.setImageResource(R.drawable.lol_guide_term)
                imageSlider.visibility = View.VISIBLE
                txt_content.visibility = View.GONE
                var images = listOf<Int>(R.drawable.img_terms01, R.drawable.img_terms02, R.drawable.img_terms03, R.drawable.img_terms04,
                        R.drawable.img_terms05, R.drawable.img_terms06, R.drawable.img_terms07, R.drawable.img_terms08, R.drawable.img_terms09,
                        R.drawable.img_terms10, R.drawable.img_terms11, R.drawable.img_terms12, R.drawable.img_terms13, R.drawable.img_terms14)
                imageSlider.setSliderAdapter(SliderAdapter(this, images))
//                imageSlider.startAutoCycle()
                imageSlider.setIndicatorAnimation(IndicatorAnimations.WORM)
                imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
            }
        }
    }

    fun logout(view: View) {
        auth.signOut()
        var intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    fun moveHome(view: View) {
        var intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }
}