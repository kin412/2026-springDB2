package hello.springtx.propagation;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.UnexpectedRollbackException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired LogRepository logRepository;


    /*
    memberService       @Transactional:OFF
    memberRepository    @Transactional:ON
    logRepository       @Transactional:ON
     */
    @Test
    void outerTxoff_success() {
        //given
        String username = "outerTxoff_success";

        //when
        memberService.joinV1(username);

        //then: 모든 데이터가 정상 저장 된다.
        assertTrue(memberRepository.find(username).isPresent());
        assertTrue(logRepository.find(username).isPresent());

    }

    /*
    memberService       @Transactional:OFF
    memberRepository    @Transactional:ON
    logRepository       @Transactional:ON Exception
     */
    @Test
    void outerTxoff_fail() {
        //given
        String username = "로그예외_outerTxoff_fail";

        //when
        assertThatThrownBy(() -> memberService.joinV1(username))
                .isInstanceOf(RuntimeException.class);

        //then
        // member만 저장되므로 데이터 정합성이 깨진다. log데이터는 롤백된다.
        assertTrue(memberRepository.find(username).isPresent());
        assertTrue(logRepository.find(username).isEmpty());

    }

    /*
    memberService       @Transactional:ON
    memberRepository    @Transactional:OFF
    logRepository       @Transactional:OFF
     */
    @Test
    void singleTx() {
        //given
        String username = "singleTx";

        //when
        memberService.joinV1(username);

        //then
        assertTrue(memberRepository.find(username).isPresent());
        assertTrue(logRepository.find(username).isPresent());

    }

    /*
    memberService       @Transactional:ON
    memberRepository    @Transactional:ON
    logRepository       @Transactional:ON
     */
    @Test
    void outerTxOn_success() {
        //given
        String username = "outerTxoff_success";

        //when
        memberService.joinV1(username);

        //then
        assertTrue(memberRepository.find(username).isPresent());
        assertTrue(logRepository.find(username).isPresent());

    }

    /*
    memberService       @Transactional:ON
    memberRepository    @Transactional:ON
    logRepository       @Transactional:ON Exception
     */
    @Test
    void outerTxOn_fail() {
        //given
        String username = "로그예외_outerTxoff_fail";

        //when
        assertThatThrownBy(() -> memberService.joinV1(username))
                .isInstanceOf(RuntimeException.class);

        //then
        // 모든데이터가 롤백된다.
        assertTrue(memberRepository.find(username).isEmpty());
        assertTrue(logRepository.find(username).isEmpty());

    }

    /*
    memberService       @Transactional:ON
    memberRepository    @Transactional:ON
    logRepository       @Transactional:ON Exception
     */
    @Test
    void recoveryException_fail() {
        //given
        String username = "로그예외_outerTxoff_fail";

        //when
        assertThatThrownBy(() -> memberService.joinV2(username))
                .isInstanceOf(UnexpectedRollbackException.class);

        //then
        // 모든데이터가 롤백된다.
        assertTrue(memberRepository.find(username).isEmpty());
        assertTrue(logRepository.find(username).isEmpty());

    }

    /*
memberService       @Transactional:ON
memberRepository    @Transactional:ON
logRepository       @Transactional:ON(REQUIRES_NEW) Exception
 */
    @Test
    void recoveryException_success() {
        //given
        String username = "로그예외_recoveryException_success";

        //when
        memberService.joinV2(username);

        //then
        // member 저장, log 롤백
        assertTrue(memberRepository.find(username).isPresent());
        assertTrue(logRepository.find(username).isEmpty());

    }


}