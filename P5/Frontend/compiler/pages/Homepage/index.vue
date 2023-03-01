<template>
    <div class="wrapper">
      <div class="content">
        <div class="header">
          <h1>PYTHON COMPILER</h1>
        </div>
        <div class="textarea">
          <h4>Please insert Python code below</h4>
          <textarea v-model="input" placeholder="Insert here" name="" id="" cols="30" rows="10"></textarea>
        </div>
        <div class="button">
          <button @click="saveCodeAndCompile">Compile and run</button>
        </div>
        <div class="output">
          <textarea v-model="output" name="" id="" cols="30" rows="10"></textarea>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    data() {
      return {
        input: '',
        output: ''
      };
    },
    methods: {
      async saveCodeAndCompile() {
        const code = {
          stringOfCode: this.input
        };
        console.log(code)
        this.output = (await axios.post("http://localhost:8080/Compile", code)).data;
        this.input = await(await(axios.get("http://localhost:8080/Test"))).data
      }
    }
  };
  </script>
  
<style scoped>

.wrapper {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    background-color: #f8fafc;
}

.content {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
}

.header h1{
    color: #404040;
    font-size: 55px;
}

.textarea {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
}

.textarea h4{
    color: #404040;
    font-size: 25px;
}

.textarea textarea {
    border: solid 2px #9370DB;
    resize: none;
    height: 15rem;
    width: 20rem;
    background-color: #E6E6FA;
    color: #27272a;
}

.button {
    margin-top: 20px;
    margin-bottom: 20px;
}

.button button{
    background-color: #4169E1;
    color: white;
    width: 12rem;
    height: 2.5rem;
}

.button button:hover{
    cursor: pointer;
}




.output textarea {
    resize: none;
    height: 15rem;
    width: 20rem;
    background-color: #E6E6FA;
    color: #27272a;
    border: solid 2px #9370DB;
}



</style>
