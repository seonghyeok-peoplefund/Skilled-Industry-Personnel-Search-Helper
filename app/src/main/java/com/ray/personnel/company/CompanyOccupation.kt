package com.ray.personnel.company

class CompanyOccupation {
    companion object{
        val occupation :Map<String, Map<String, Int>> =
                mapOf(
                        "NONE" to mapOf("NONE" to -1),
                        "개발" to mapOf(
                                "서버 개발자" to 872,
                                "웹 개발자" to 873,
                                "프론트엔드 개발자" to 669,
                                "자바 개발자" to 660,
                                "안드로이드 개발자" to 677,
                                "소프트웨어 엔지니어" to 10110,
                                "iOS 개발자" to 678,
                                "파이썬 개발자" to 899,
                                "데이터 엔지니어" to 655,
                                "Node.js 개발자" to 895,
                                "DevOps / 시스템 관리자" to 674,
                                "머신러닝 엔지니어" to 1634,
                                "시스템,네트워크 관리자" to 665,
                                "C,C++ 개발자" to 900,
                                "데이터 사이언티스트" to 1024,
                                "빅데이터 엔지니어" to 1025,
                                "개발 매니저" to 877,
                                "QA,테스트 엔지니어" to 676,
                                "PHP 개발자" to 893,
                                "기술지원" to 1026,
                                "프로덕트 매니저" to 876,
                                "보안 엔지니어" to 671,
                                "웹 퍼블리셔" to 939,
                                "하드웨어 엔지니어" to 672,
                                ".NET 개발자" to 661,
                                "블록체인 플랫폼 엔지니어" to 1027,
                                "임베디드 개발자" to 658,
                                "영상,음성 엔지니어" to 896,
                                "크로스플랫폼 앱 개발자" to 10111,
                                "루비온레일즈 개발자" to 894,
                                "BI 엔지니어" to 1022,
                                "CTO,Chief Technology Officer" to 795,
                                "그래픽스 엔지니어" to 898,
                                "VR 엔지니어" to 10112,
                                "CIO,Chief Information Officer" to 793),
                        "경영/비즈니스" to mapOf("서비스 기획자" to 565,
                                "사업개발·기획자" to 564,
                                "PM·PO" to 559,
                                "전략 기획자" to 563,
                                "운영 매니저" to 554,
                                "경영지원" to 552,
                                "데이터 분석가" to 656,
                                "회계·경리" to 1034,
                                "컨설턴트" to 570,
                                "총무" to 562,
                                "해외 사업개발·기획자" to 10115,
                                "자금담당" to 10116,
                                "조직관리" to 567,
                                "오피스 관리" to 557,
                                "리스크 관리 전문가" to 569,
                                "경영 혁신가" to 568,
                                "사무보조" to 10122,
                                "지역 관리 매니저" to 558,
                                "구매담당" to 561,
                                "COO,Chief Operation Officer" to 794,
                                "CFO,Chief Financial Officer" to 792,
                                "세미나/포럼 기획자" to 10118,
                                "비서" to 550,
                                "CSO,Chief Strategy Officer" to 798,
                                "전시 기획자" to 10117,
                                "애자일코치" to 10120,
                                "지점장" to 560,
                                "CEO,Chief Executive Officer" to 791,
                                "안내원" to 555,
                                "사내 심리상담가" to 10121,
                                "공연 기획자" to 10119),
                        "마케팅/광고" to mapOf(
                                "디지털 마케터" to 1030,
                                "마케터" to 710,
                                "콘텐츠 마케터" to 1635,
                                "퍼포먼스 마케터" to 10138,
                                "마케팅 전략 기획자" to 719,
                                "브랜드 마케터" to 707,
                                "광고 기획자(AE)" to 763,
                                "소셜 마케터" to 721,
                                "글로벌 마케팅" to 950,
                                "그로스 해커" to 717,
                                "모바일마케팅" to 1033,
                                "PR 전문가" to 714,
                                "마케팅 디렉터" to 1032,
                                "카피라이터" to 708,
                                "키워드광고" to 722,
                                "마켓 리서처" to 709,
                                "제휴" to 720,
                                "CMO,Chief Marketing Officer" to 799,
                                "ATL 마케터" to 715,
                                "BTL 마케터" to 716,
                                "CBO,Chief Brand Officer" to 801,
                                "Sports 전문가" to 1031),
                        "디자인" to mapOf("UX 디자이너" to 599,
                                "UI,GUI 디자이너" to 597,
                                "웹 디자이너" to 594,
                                "그래픽 디자이너" to 592,
                                "모바일 디자이너" to 595,
                                "BI/BX 디자이너" to 879,
                                "광고 디자이너" to 1029,
                                "제품 디자이너" to 603,
                                "영상,모션 디자이너" to 602,
                                "패키지 디자이너" to 10130,
                                "3D 디자이너" to 929,
                                "아트 디렉터" to 593,
                                "일러스트레이터" to 596,
                                "2D 디자이너" to 928,
                                "캐릭터 디자이너" to 951,
                                "산업 디자이너" to 600,
                                "패션 디자이너" to 953,
                                "출판, 편집 디자이너" to 952,
                                "인테리어 디자이너" to 601,
                                "공간 디자이너" to 606,
                                "패브릭 디자이너" to 10128,
                                "VMD" to 10131,
                                "전시 디자이너" to 10129,
                                "조경 디자이너" to 605,
                                "가구 디자이너" to 10127),
                        "영업" to mapOf("MD" to 758,
                                "CS 매니저" to 1028,
                                "서비스 운영" to 10126,
                                "CS 어드바이저" to 586,
                                "리테일 MD" to 760,
                                "패션 MD" to 759,
                                "CRM 전문가" to 901,
                                "인바운드 텔레마케터" to 769,
                                "매장 관리자" to 752,
                                "매장점원" to 754,
                                "이벤트 기획자" to 639,
                                "헬스케어매니저" to 1637,
                                "여행 에이전트" to 641,
                                "가맹점 관리자" to 762,
                                "아웃바운드 텔레마케터" to 902,
                                "비주얼머천다이저" to 757,
                                "리셉션" to 10125,
                                "승무원" to 589,
                                "AS 기술자" to 591,
                                "플로리스트" to 640,
                                "피부관리사" to 755,
                                "헤어 디자이너" to 756,
                                "미용사" to 10123,
                                "애견 미용사" to 10124),
                        "고객서비스/리테일" to mapOf(   "기업영업" to 1036,
                                "외부영업" to 766,
                                "영업 관리자" to 954,
                                "기술영업" to 770,
                                "주요고객사 담당자" to 768,
                                "솔루션 컨설턴트" to 1035,
                                "해외영업" to 955,
                                "고객성공매니저" to 1037,
                                "미디어 세일즈" to 1633,
                                "내부영업" to 767,
                                "세일즈 엔지니어" to 773,
                                "의료기기 영업" to 772,
                                "제약영업" to 771),
                        "미디어" to mapOf(
                                "콘텐츠 크리에이터" to 1046,
                                "영상 편집가" to 723,
                                "PD" to 727,
                                "에디터" to 725,
                                "비디오 제작" to 3351,
                                "작가" to 724,
                                "출판 기획자" to 956,
                                "통·번역" to 940,
                                "큐레이터" to 1045,
                                "저널리스트" to 726,
                                "음향 엔지니어" to 957,
                                "라이센스 관리자" to 1636,
                                "사진작가" to 729,
                                "리포터" to 728),
                        "인사" to mapOf(
                                "인사담당" to 643,
                                "리크루터" to 644,
                                "조직문화" to 649,
                                "평가·보상" to 645,
                                "헤드헌터" to 1041,
                                "HRBP" to 1043,
                                "급여담당" to 650,
                                "노무·노사" to 647,
                                "HRD" to 648,
                                "HR 컨설턴트" to 642,
                                "기술 교육" to 611,
                                "E-러닝" to 651,
                                "교사" to 608,
                                "사내 강사" to 1044,
                                "교수" to 1042),
                        "게임 제작" to mapOf(

                                "게임 기획자" to 892,
                                "게임 그래픽 디자이너" to 880,
                                "게임 클라이언트 개발자" to 961,
                                "모바일 게임 개발자" to 962,
                                "유니티 개발자" to 878,
                                "게임 아티스트" to 881,
                                "게임 서버 개발자" to 960,
                                "게임운영자(GM)" to 958,
                                "언리얼 개발자" to 897),
                        "금융" to mapOf(
                                "재무 담당자" to 1048,
                                "회계담당" to 534,
                                "재무 분석가" to 538,
                                "IR" to 920,
                                "투자·증권" to 10056,
                                "애널리스트" to 882,
                                "자산운용가" to 936,
                                "공인회계사" to 542,
                                "트레이더" to 933,
                                "금융공학자" to 934,
                                "자산관리사" to 935,
                                "내부통제 담당자" to 547,
                                "준법감시인" to 938,
                                "부동산 자산 관리자" to 742,
                                "감정평가사" to 745,
                                "세무사" to 1047,
                                "부동산 중개사" to 743,
                                "보험 에이전트" to 681,
                                "계리사" to 683,
                                "투자은행가" to 937,
                                "손해 사정관" to 682,
                                "언더라이터" to 684,
                                "청구 담당자" to 1049),
                        "물류/무역" to mapOf(
                                "물류담당" to 783,
                                "입·출고 관리자" to 10148,
                                "물류 분석가" to 782,
                                "유통 관리자" to 10149,
                                "수출입사무" to 10151,
                                "운송 관리자" to 786,
                                "웨어하우스" to 787,
                                "바이어관리·상담·개발" to 10150,
                                "배송담당" to 777,
                                "무역사무" to 10053,
                                "지게차 운전사" to 780,
                                "운전기사" to 781,
                                "선적,발송 사무원" to 785,
                                "항공 운송" to 788,
                                "해운·해양 운송" to 789,
                                "디젤 정비공" to 778,
                                "운행 관리원" to 779,
                                "화물트럭 운전기사" to 784,
                                "보세사" to 790,
                                "관세사" to 10139,
                                "원산지관리사" to 10140),
                        "엔지니어링/설계" to mapOf(
                                "연구원" to 739,
                                "생명공학 연구원" to 740,
                                "약학 분석 화학자" to 731,
                                "임상시험 연구원" to 735,
                                "유전공학자" to 738,
                                "수의사" to 634,
                                "임상시험 간호사" to 736,
                                "임상병리사" to 626,
                                "간호사" to 627,
                                "연구실 기사" to 732,
                                "수의 테크니션" to 632,
                                "미생물학자" to 737,
                                "임상심리사" to 10147,
                                "치과 위생사" to 622,
                                "증례 관리자" to 623,
                                "간병인" to 624,
                                "조무사" to 625,
                                "작업 치료사" to 629,
                                "물리 치료사" to 630,
                                "호흡장애 치료사" to 631,
                                "의사" to 633,
                                "간호 조무사" to 635,
                                "치과의사" to 636,
                                "검안사" to 637,
                                "약사" to 733,
                                "약사 보조원" to 734,
                                "한의사" to 1040,
                                "방사선사" to 10054,
                                "병원 코디네이터" to 10134,
                                "의무기록사" to 10135),
                        "의료/제약/바이오" to mapOf(
                                "기계 엔지니어" to 843,
                                "전자 엔지니어" to 823,
                                "전기 엔지니어" to 821,
                                "전기기계 공학자" to 822,
                                "로봇·자동화" to 10145,
                                "자동차 공학자" to 804,
                                "제어 엔지니어" to 817,
                                "제품 엔지니어" to 856,
                                "항공우주 공학자" to 802,
                                "설비 엔지니어" to 828,
                                "토목기사" to 811,
                                "공정 엔지니어" to 855,
                                "QA 엔지니어" to 859,
                                "RF 엔지니어" to 864,
                                "화학공학 엔지니어" to 809,
                                "건설 엔지니어" to 815,
                                "장비 엔지니어" to 826,
                                "소방안전 기술자" to 830,
                                "프로젝트 엔지니어" to 857,
                                "회전기계 엔지니어" to 865,
                                "도면 담당자" to 810,
                                "도면 작성가" to 819,
                                "환경 엔지니어" to 824,
                                "환경 안전기사" to 825,
                                "지리정보시스템" to 831,
                                "산업 엔지니어" to 836,
                                "인허가 담당 엔지니어" to 839,
                                "생산공학 엔지니어" to 840,
                                "재료공학자" to 842,
                                "선박 공학자" to 847,
                                "석유공학 엔지니어" to 850,
                                "배관설계 엔지니어" to 852,
                                "사업수주 엔지니어" to 858,
                                "QC 엔지니어" to 860,
                                "구조공학 엔지니어" to 867,
                                "CAD·3D 설계자" to 10132,
                                "농업 공학자" to 803,
                                "유전 공학자" to 805,
                                "생물의학자" to 806,
                                "보일러 엔지니어" to 807,
                                "세라믹 엔지니어" to 808,
                                "시운전 엔지니어" to 812,
                                "시추 엔지니어" to 820,
                                "지리학자" to 832,
                                "지질공학자" to 833,
                                "보건안전 엔지니어" to 834,
                                "고압설계 엔지니어" to 835,
                                "I&C 엔지니어" to 838,
                                "해양공학자" to 841,
                                "금속 공학자" to 844,
                                "광산 기술자" to 845,
                                "광산 안전 기술자" to 846,
                                "원자력·에너지" to 848,
                                "플랜트 엔지니어" to 853,
                                "플라스틱 엔지니어" to 854,
                                "전자계전 엔지니어" to 861,
                                "저수처리 엔지니어" to 863,
                                "터빈공학자" to 869),
                        "제조/생산" to mapOf(
                                "품질 관리자" to 704,
                                "생산 관리자" to 701,
                                "공정 관리자" to 703,
                                "생산직 종사자" to 702,
                                "섬유·의류·패션" to 10113,
                                "자재관리·구매" to 699,
                                "화학자" to 696,
                                "제조 엔지니어" to 698,
                                "기계·설비·설계" to 700,
                                "안전 관리자" to 705,
                                "제조 테스트 엔지니어" to 706,
                                "반도체·디스플레이" to 10114,
                                "조립 기술자" to 695,
                                "기계제작 기술자" to 697),
                        "교육" to mapOf(
                                "식품 MD" to 761,
                                "식품가공·개발" to 10153,
                                "메뉴개발" to 10058,
                                "외식업 종사자" to 749,
                                "요리사" to 748,
                                "바텐더" to 747,
                                "레스토랑 관리자" to 750,
                                "푸드스타일리스트" to 10141,
                                "바리스타" to 10154,
                                "영양사" to 628,
                                "제과·제빵사" to 746,
                                "소믈리에" to 10152),
                        "식/음료" to mapOf(

                                "교재·교육기획" to 10103,
                                "전문강사" to 10102,
                                "외국어교육" to 10106,
                                "자격증·기술전문교육" to 10109,
                                "학원강사" to 10104,
                                "교직원" to 10105,
                                "초·중·고 교사" to 10108,
                                "유치원·보육교사" to 10107),
                        "법률/법집행기관" to mapOf(
                                "법무담당" to 1002,
                                "변호사" to 691,
                                "법무 자문위원" to 692,
                                "법무사" to 694,
                                "특허담당" to 10137,
                                "수사관" to 686,
                                "보호 관찰관" to 687,
                                "경찰관" to 688,
                                "보안요원" to 689,
                                "경호원" to 690,
                                "법무관" to 693,
                                "변리사" to 10136),
                        "건설/시설" to mapOf(
                                "실내건축" to 10142,
                                "건축시공·감리" to 10143,
                                "현장 소장" to 572,
                                "목수" to 571,
                                "중장비 기술자" to 573,
                                "정비공" to 574,
                                "유지보수 관리자" to 575,
                                "관리인" to 576,
                                "전기 기술자" to 577,
                                "인스톨러" to 578,
                                "페인터" to 579,
                                "플랜트 관리자" to 580,
                                "용접기사" to 581,
                                "배관공" to 582,
                                "연관공" to 583,
                                "견적 기술자" to 584,
                                "측량·계측" to 585,
                                "건축가" to 604,
                                "건설 안전·품질·검사" to 10144),
                        "공공/복지" to mapOf(

                                "카운셀러" to 609,
                                "정보 분석가" to 617,
                                "공무원" to 620,
                                "소방관" to 615,
                                "환경 전문가" to 616,
                                "인명 구조원" to 618,
                                "지역 전문가" to 619,
                                "직업군인" to 621,
                                "자원봉사자" to 730,
                                "사회복지사" to 10055,
                                "응급구조사" to 10133,
                                "요양보호사" to 10146)


                )

    }
}