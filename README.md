   # CapStone 프로젝트 환경
## FE

- 
-

# 개발 가이드

## GIT

### **Issue**

- 작업단위별로 `Issue` 생성
- 제목은 `[타입] 제목` 형식으로 작성

    ex) [기능개발] 로그인 기능 개발

    타입목록

    - 화면개발
    - 화면수정
    - 기능개발
    - 기능수정
    - 오류수정
- 작업자 `Assignees` 추가
- `Labels` 추가
- 내용기입

    ```
    CRUD
    - [ ] Create
    - [ ] Read
    - [ ] Update
    - [ ] Delete
    ```

### **Branch**

- 기본브랜치는 `develop`
- 배포의 경우 `master` 브랜치에서 진행
- 이슈에 맞춰 브랜치생성
    - branch 이름은 `feature/{issue number}`
- 이슈해결시 상위 브랜치에 `Pull Requests` 생성

### **Pull Requests**

- 제목은 `[타입] 제목` 형식으로 작성
- 작업자 `Assignees` 추가
- 해당 브랜치에 해당하는 `Labels` 추가
- 해당 브랜치에서 작업한 내용 간략히 적어 제출
- 코드리뷰 진행 후 `Merge`
    - 충돌 및 반려시 회수후 코드 수정 후 PR 올림

        [github pull request 취소하고 수정하여 다시 pull request하기.](https://wizardfactory.tumblr.com/post/119735176581/github-pull-request-%EC%B7%A8%EC%86%8C%ED%95%98%EA%B3%A0-%EC%88%98%EC%A0%95%ED%95%98%EC%97%AC-%EB%8B%A4%EC%8B%9C-pull-request%ED%95%98%EA%B8%B0)

- 승인되면 PR작성자가 `merge` 후 branch 삭제 
- 다시 issue 등록, 브랜치 생성 후 작업 진행 

   # 협업도구 NOTION
### 초대 링크 : https://www.notion.so/invite/ce6b66be3ec2fce3b55b6a15453788e1fba9cc91
### NOTION 링크 : https://www.notion.so/565a43e0f04f46a6b2cc7f2ce1539a5d?v=66bceeeab63a4395a5b18809169ac13a
    - 참고사항 : 노션 사용이 처음이라 서툽니다! 피드백 환영합니다. 많이 쓰이는 도구인만큼, 익숙해지는 것이 좋아보입니다. :) 

   # CapStone 회의록 
### 2022.05.20 19:30

- 회의 내용 :  각자 작업 환경에서 STT testing 진행 후, 추후 회의날짜 정할 예정입니다. (참고 링크 : https://stickode.tistory.com/129 )
