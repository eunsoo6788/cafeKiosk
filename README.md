# 카페 키오스크  
테스트 코드 작성과 관련된 예제 프로젝트 입니다. 
프로젝트는 `카페 키오스크` 기능을 간단하게 구현하였습니다.

---
## 학습 목표

### 1. 서비스 운영 중에 장애가 발생 했다면, 반드시 테스트 코드를 추가해서 재발을 방지하자.

저는 테스트 코드 작성에 있어 이 부분이 가장 중요하다고 생각합니다. 테스트 코드를 작성하는 여러 장점 중 하나가 코드를 수정함에 있어 사이드 이펙트가 발생하지 않는 다는 것을 검증 받을수 있다는 것이라고 생각합니다. 하지만 운영중인 서비스에서 장애가 발생했다면 QA, 개발, 기획 등 대다수의 사람들이 놓쳤던 부분이며, 지속적으로 장애가 발생할 가능성이 있다고 생각합니다. 따라서 장애가 발생한 코드는 반드시 테스트 코드를 작성하여, 동일한 장애가 다시 발생하지 않도록 검증하는 것이 중요하다고 생각합니다.

### 2. Mock, Any 사용을 최대한 지양하자.

테스트 코드 작성에 회의적인 대부분의 사람들은 시간 들여 테스트 코드를 작성하였지만, 그 보상을 충분히 받지 못하여 회의적인 것이라고 생각합니다. 저는 이러한 보상을 받지 못하는 원인 중 하나가 무분별한 Mock, Any 사용이라고 생각합니다. 너무 지나치게 Mock과 Any를 사용해 버리면 정작 테스트 코드로 검증해야 할 부분을 올바르게 검증하지 못한다는 것입니다.

### 3. @DisplayName을 자세하게 작성하자

내가 작성한 테스트 코드는 팀의 자산 입니다. 또한 팀과 함께 공유하는 문서 입니다. 테스트 코드 메소드 명으로는 정확하게 테스트 코드가 검증하려고 하는 것을 이해하기 힘들 수 있습니다. 따라서 @DisplayName을 활용하여 자세히 설명 붙여 준다면, 다른 팀원들이 이해하기 더 쉬울 것라고 생각합니다.
비추천 : 음료 1개 추가 테스트
추천 : 음료를 1개 추가하면 주문 목록에 담긴다. (테스트 행위에 대한 결과까지 문장으로 기술)

### 4. 경계값 테스트를 위해 FixtureMonkey를 사용해 보자.

테스트 코드를 작성하기 위해 저희는 fixture를 생성합니다. 하지만 저희가 생성한 fixture들은 당연히 테스트 코드를 통과하는 fixture입니다. 정말 장애가 발생하는 fixture는 경계값에서 대부분 발생합니다. 네이버에서 개발한 경계값 생성에 도움을 주는 라이브러리가 있습니다.
FixtureMonkey를 통해 경계값 까지 검증 가능한 테스트 코드 작성을 추천드립니다.  
네이버의 FixtureMonkey 사용기

### 5. BDDMockito를 사용하자. (권장)

BDDMockito에 대해 알아보기 전에 BDD는 TDD에서 파생된 개발 방법론 입니다. 또한 함수 단위의 테스트에 집중하기 보다 시나리오에 기반한 테스트 케이스 자체에 집중하는 테스틑 하는 방법입니다. 많은 개발자 분들이 테스트 코드를 작성할때 무의식 적으로 given-when-then 구조를 많이 활용하실 텐데요.

일반 적인 Mockito를 사용하신다면 given에서 when().thenReturn(); 으로 사용이 됩니다. 약간 어색하지 않나요? given절에서 when을 사용하는게 저는 조금 어색하게 느껴집니다. BDDMockito는 given절에서 given().willReturn(); 형식으로 사용해서 조금더 given-when-then 구조에 맞게끔 네이밍이 되어 있습니다. 동작에는 차이가 없습니다.
Mockito : when(mailSendClient.send(any()).thenReturn(true);
BDDMockito : given(mailSendClient.send(any()).willReturn(true);

### 1. Junit의 Assertion 보다는 AssertJ를 사용하자
JUnit은 기본적인 검증 기능을 제공하지만, AssertJ는 다양하고 가독성이 뛰어난 검증 기능을 제공합니다. AssertJ는 Fluent 형식의 스타일로 작성하기 때문에 가독성이 뛰어나며, 다양한 검증 기능들을 제공합니다.

**Junit Assertion**
```java
    @Test
    @DisplayName("Junit의 Assertions을 검증한다.")
    void testJunitAssertions() {
        int expected = 10;
        int actual = 10;

        assertEquals(expected, actual); // 두 값이 같은지 확인
        assertNotEquals(5, actual); // 값이 다른지 확인
        assertTrue(actual > 5); // 조건이 true인지 확인
        assertFalse(actual < 5); // 조건이 false인지 확인
    }
```

**AssertJ Assertion**
```java
    @Test
    @DisplayName("AssertJ의 기본적인 Assertions를 검증한다.")
    void testAssertJAssertions() {
        List<String> colors = List.of("Red", "Green", "Blue");

        assertThat(colors)
            .isNotEmpty()
            .hasSize(3)
            .contains("Red") // 특정 값 포함 여부
            .containsExactly("Red", "Green", "Blue") // 정확한 순서 검증
            .containsExactlyInAnyOrder("Blue", "Green", "Red"); // 순서 무관 검증
        
    }
    
    @Test
    @DisplayName("AssertJ의 에러 처리와 관련된 Assertions를 검증한다.")
    void testExceptionAssertions() {
        assertThatThrownBy(() -> {
            throw new IllegalArgumentException("Invalid input");
        })
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Invalid");
    }
```


